#!/bin/bash
#
# Force rename: sequro / Sequro / SEQURO  ->  ams / AMS / AMS
# Run from the Maven/Spring project root (folder that contains pom.xml).
#
# Usage (Git Bash):
#   cd "/c/MULTI-CURRENCY1/MultiCurrencyCodeBackup/SequroCMSAccountManagementService_app"
#   bash ../rename-sequro-to-ams.sh
#   # or, from MultiCurrencyCodeBackup:
#   bash rename-sequro-to-ams.sh SequroCMSAccountManagementService_app
#

set -euo pipefail

ROOT="${1:-.}"
ROOT="$(cd "$ROOT" && pwd)"

OLD_LOWER="sequro"
NEW_LOWER="ams"
OLD_CAP="Sequro"
NEW_CAP="AMS"
OLD_UPPER="SEQURO"
NEW_UPPER="AMS"

# Git Bash on Windows: GNU sed uses -i without backup extension
if sed --version 2>/dev/null | grep -qi gnu; then
  SED_I=(-i)
else
  SED_I=(-i '')
fi

TEXT_EXT_RE='\.(java|xml|properties|yml|yaml|json|html|htm|js|jsx|ts|tsx|css|scss|md|txt|sql|sh|bat|cmd|gradle|kt|kts|vm|ftl|ini|cfg|conf|launch|cs|csproj|sln|config|aspx|ascx|wsdl|xsd|pom|mf|gitignore|gitattributes|editorconfig)$'

EXCLUDE_DIR_RE='/(\\.git|target|build|node_modules|\\.metadata|\\.idea|bin|obj)(/|$)'

replace_in_name() {
  local name="$1"
  name=$(printf '%s' "$name" | sed \
    -e "s/${OLD_UPPER}/${NEW_UPPER}/g" \
    -e "s/${OLD_CAP}/${NEW_CAP}/g" \
    -e "s/${OLD_LOWER}/${NEW_LOWER}/g")
  printf '%s' "$name"
}

is_text_file() {
  local f="$1"
  [[ "$f" =~ $TEXT_EXT_RE ]] && return 0
  # extensionless files that are often text in Java projects
  case "$(basename "$f")" in
    Dockerfile|Makefile|mvnw) return 0 ;;
  esac
  return 1
}

should_skip_path() {
  local p="$1"
  [[ "$p" =~ $EXCLUDE_DIR_RE ]]
}

echo "========================================="
echo " Renaming project: sequro -> AMS / ams"
echo " Root: $ROOT"
echo "========================================="

cd "$ROOT"

echo "Step 0: Removing build output folders..."
find . -type d \( -name target -o -name build \) \
  ! -path "*/.git/*" -prune -o -type d \( -name target -o -name build \) -print 2>/dev/null \
  | sort -r | while read -r d; do
    rm -rf "$d" 2>/dev/null || true
  done

echo "Step 1: Replacing text inside source files..."
# sed is case-sensitive (unlike PowerShell -replace). Order: SEQURO, Sequro, sequro.
find . -type f ! -path "*/.git/*" | while IFS= read -r file; do
  should_skip_path "$file" && continue
  is_text_file "$file" || continue
  if grep -qE "${OLD_LOWER}|${OLD_CAP}|${OLD_UPPER}" "$file" 2>/dev/null; then
    sed "${SED_I[@]}" \
      -e "s/${OLD_UPPER}/${NEW_UPPER}/g" \
      -e "s/${OLD_CAP}/${NEW_CAP}/g" \
      -e "s/${OLD_LOWER}/${NEW_LOWER}/g" \
      "$file"
  fi
done

echo "Step 1b: Fixing package folder AMS -> ams (Windows-safe)..."
for pkgroot in ./src/main/java/AMS ./src/test/java/AMS; do
  if [[ -d "$pkgroot" ]]; then
    mv "$pkgroot" "${pkgroot}__tmp"
    mv "${pkgroot}__tmp" "${pkgroot%/AMS}/ams"
  fi
done
find . -type f ! -path "*/.git/*" | while IFS= read -r file; do
  should_skip_path "$file" && continue
  is_text_file "$file" || continue
  grep -q 'AMS\.cms' "$file" 2>/dev/null || continue
  sed "${SED_I[@]}" 's/AMS\.cms/ams.cms/g' "$file"
done

echo "Step 2: Renaming files (Sequro + sequro + SEQURO)..."
find . -depth -type f ! -path "*/.git/*" | while IFS= read -r file; do
  should_skip_path "$file" && continue
  base=$(basename "$file")
  newbase=$(replace_in_name "$base")
  if [[ "$base" != "$newbase" ]]; then
    dir=$(dirname "$file")
    dest="$dir/$newbase"
    if [[ -e "$dest" && "$file" != "$dest" ]]; then
      echo "  SKIP (exists): $file -> $dest"
    else
      mv "$file" "$dest"
      echo "  FILE: $base -> $newbase"
    fi
  fi
done

echo "Step 3: Renaming directories..."
find . -depth -type d ! -path "*/.git/*" ! -path "." | while IFS= read -r dir; do
  should_skip_path "$dir" && continue
  base=$(basename "$dir")
  newbase=$(replace_in_name "$base")
  if [[ "$base" != "$newbase" ]]; then
    parent=$(dirname "$dir")
    dest="$parent/$newbase"
    if [[ -e "$dest" && "$dir" != "$dest" ]]; then
      echo "  SKIP (exists): $dir -> $dest"
    else
      # Windows (case-insensitive): two-step rename when only case changes
      if [[ "$(echo "$base" | tr '[:upper:]' '[:lower:]')" == "$(echo "$newbase" | tr '[:upper:]' '[:lower:]')" ]]; then
        tmp="$parent/.rename_tmp_$$"
        mv "$dir" "$tmp"
        mv "$tmp" "$dest"
      else
        mv "$dir" "$dest"
      fi
      echo "  DIR:  $base -> $newbase"
    fi
  fi
done

echo "Step 4: Verifying remaining references..."
MATCHES=$(grep -RinE "sequro|Sequro|SEQURO" . \
  --exclude-dir=.git \
  --exclude-dir=target \
  --exclude-dir=build \
  --exclude-dir=node_modules \
  --exclude-dir=.metadata \
  2>/dev/null || true)

if [[ -z "$MATCHES" ]]; then
  echo "========================================="
  echo " SUCCESS: No sequro/Sequro/SEQURO references"
  echo "========================================="
else
  echo "========================================="
  echo " Remaining references (review manually):"
  echo "========================================="
  echo "$MATCHES"
fi

echo "Step 5: Optional Maven build..."
if [[ -f "pom.xml" ]]; then
  if command -v mvn >/dev/null 2>&1; then
    mvn -q clean compile -DskipTests || echo "Maven build failed — fix references above, then rebuild."
  else
    echo "mvn not in PATH — skip build or run: mvn clean install -DskipTests"
  fi
fi

echo "Step 7: Fixing logger field shadowing (SequroLogger -> AMSLogger)..."
# Field must NOT be named AMSLogger when type is AMSLogger (Java forward-reference error)
find . -type f -name '*.java' ! -path '*/.git/*' | while IFS= read -r file; do
  grep -q 'AMSLogger AMSLogger' "$file" 2>/dev/null || continue
  sed "${SED_I[@]}" \
    -e 's/private static AMSLogger AMSLogger =/private static AMSLogger amsLogger =/g' \
    -e 's/private AMSLogger AMSLogger =/private AMSLogger amsLogger =/g' \
    -e 's/AMSLogger\.writeInfoLog/amsLogger.writeInfoLog/g' \
    -e 's/AMSLogger\.writeExceptionLog/amsLogger.writeExceptionLog/g' \
    "$file"
done

echo "========================================="
echo " Rename finished"
echo "========================================="
