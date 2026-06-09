# Force rename: sequro / Sequro / SEQURO -> ams / AMS / AMS
# Run in PowerShell from the project folder or pass -ProjectRoot.

param(
    [string]$ProjectRoot = "c:\MULTI-CURRENCY1\MultiCurrencyCodeBackup\SequroCMSAccountManagementService_app"
)

$ErrorActionPreference = "Stop"

function Replace-Name([string]$name) {
    # Case-sensitive (-creplace): lowercase package dir stays "ams", not "AMS"
    $name -creplace 'SEQURO', 'AMS' -creplace 'Sequro', 'AMS' -creplace 'sequro', 'ams'
}

function Replace-Text([string]$content) {
    $content -creplace 'SEQURO', 'AMS' -creplace 'Sequro', 'AMS' -creplace 'sequro', 'ams'
}

function Test-ExcludedPath([string]$fullPath) {
    $fullPath -match '\\(\.git|target|build|node_modules|\.metadata|\.idea|bin|obj)(\\|$)'
}

$textExtensions = @(
    '.java', '.xml', '.properties', '.yml', '.yaml', '.json',
    '.html', '.htm', '.js', '.jsx', '.ts', '.tsx', '.css', '.scss',
    '.md', '.txt', '.sql', '.sh', '.bat', '.cmd', '.gradle', '.kt', '.kts',
    '.vm', '.ftl', '.ini', '.cfg', '.conf', '.launch', '.cs', '.csproj',
    '.sln', '.config', '.aspx', '.ascx', '.wsdl', '.xsd', '.pom', '.mf'
)

if (-not (Test-Path $ProjectRoot)) {
    throw "Project path not found: $ProjectRoot"
}

Set-Location $ProjectRoot
Write-Host "Root: $ProjectRoot"

Write-Host "Step 0: Removing target/build..."
Get-ChildItem -Recurse -Directory -Force -ErrorAction SilentlyContinue |
    Where-Object { $_.Name -in @('target', 'build') -and -not (Test-ExcludedPath $_.FullName) } |
    Sort-Object { $_.FullName.Length } -Descending |
    ForEach-Object { Remove-Item $_.FullName -Recurse -Force -ErrorAction SilentlyContinue }

Write-Host "Step 1: Replacing file contents..."
Get-ChildItem -Recurse -File -Force | Where-Object { -not (Test-ExcludedPath $_.FullName) } | ForEach-Object {
    $ext = $_.Extension.ToLowerInvariant()
    if ($textExtensions -notcontains $ext -and $_.Name -notin @('Dockerfile', 'Makefile', 'mvnw')) { return }
    $content = Get-Content -LiteralPath $_.FullName -Raw -Encoding UTF8 -ErrorAction SilentlyContinue
    if ($null -eq $content) { return }
    if ($content -notmatch 'sequro|Sequro|SEQURO') { return }
    $newContent = Replace-Text $content
    if ($newContent -ne $content) {
        Set-Content -LiteralPath $_.FullName -Value $newContent -Encoding UTF8 -NoNewline
    }
}

Write-Host "Step 1b: Normalizing package id (ams.cms, not AMS.cms)..."
Get-ChildItem -Recurse -File -Force | Where-Object { -not (Test-ExcludedPath $_.FullName) } | ForEach-Object {
    $ext = $_.Extension.ToLowerInvariant()
    if ($textExtensions -notcontains $ext -and $_.Name -notin @('.project', '.classpath')) { return }
    $content = Get-Content -LiteralPath $_.FullName -Raw -Encoding UTF8 -ErrorAction SilentlyContinue
    if ($null -eq $content -or -not $content.Contains('AMS.cms')) { return }
    Set-Content -LiteralPath $_.FullName -Value ($content.Replace('AMS.cms', 'ams.cms')) -Encoding UTF8 -NoNewline
}

foreach ($javaRoot in @(
    (Join-Path $ProjectRoot 'src\main\java\AMS'),
    (Join-Path $ProjectRoot 'src\test\java\AMS')
)) {
    if (Test-Path -LiteralPath $javaRoot) {
        $tmp = "${javaRoot}__rename_tmp"
        Rename-Item -LiteralPath $javaRoot -NewName ([IO.Path]::GetFileName($tmp))
        Rename-Item -LiteralPath $tmp -NewName 'ams'
    }
}

Write-Host "Step 2: Renaming files..."
Get-ChildItem -Recurse -File -Force |
    Where-Object { -not (Test-ExcludedPath $_.FullName) } |
    Sort-Object { $_.FullName.Length } -Descending |
    ForEach-Object {
        $newName = Replace-Name $_.Name
        if ($newName -eq $_.Name) { return }
        $dest = Join-Path $_.DirectoryName $newName
        if ((Test-Path -LiteralPath $dest) -and ($dest -ne $_.FullName)) {
            Write-Host "  SKIP (exists): $($_.FullName)"
            return
        }
        Rename-Item -LiteralPath $_.FullName -NewName $newName
        Write-Host "  FILE: $($_.Name) -> $newName"
    }

Write-Host "Step 3: Renaming directories..."
Get-ChildItem -Recurse -Directory -Force |
    Where-Object { -not (Test-ExcludedPath $_.FullName) } |
    Sort-Object { $_.FullName.Length } -Descending |
    ForEach-Object {
        $newName = Replace-Name $_.Name
        if ($newName -eq $_.Name) { return }
        $dest = Join-Path $_.Parent.FullName $newName
        if ((Test-Path -LiteralPath $dest) -and ($dest -ne $_.FullName)) {
            Write-Host "  SKIP (exists): $($_.FullName)"
            return
        }
        Rename-Item -LiteralPath $_.FullName -NewName $newName
        Write-Host "  DIR:  $($_.Name) -> $newName"
    }

Write-Host "Step 4: Verifying..."
$matches = Select-String -Path (Get-ChildItem -Recurse -File -Force |
    Where-Object { -not (Test-ExcludedPath $_.FullName) }).FullName `
    -Pattern 'sequro|Sequro|SEQURO' -ErrorAction SilentlyContinue

if (-not $matches) {
    Write-Host "SUCCESS: No sequro references in project files."
} else {
    Write-Host "Remaining references:"
    $matches | Select-Object -First 50 | ForEach-Object { Write-Host $_.Path ":" $_.LineNumber ":" $_.Line.Trim() }
}

Write-Host "Step 6: Fixing logger field shadowing..."
Get-ChildItem -Recurse -Filter *.java -Force | Where-Object { -not (Test-ExcludedPath $_.FullName) } | ForEach-Object {
    $c = Get-Content -LiteralPath $_.FullName -Raw -Encoding UTF8
    if (-not $c.Contains('AMSLogger AMSLogger') -and
        -not $c.Contains('AMSLogger.writeInfoLog') -and
        -not $c.Contains('AMSLogger.writeExceptionLog')) { return }
    $n = $c.Replace('private static AMSLogger AMSLogger =', 'private static AMSLogger amsLogger =')
    $n = $n.Replace('private AMSLogger AMSLogger =', 'private AMSLogger amsLogger =')
    $n = $n.Replace('AMSLogger.writeInfoLog', 'amsLogger.writeInfoLog')
    $n = $n.Replace('AMSLogger.writeExceptionLog', 'amsLogger.writeExceptionLog')
    if ($n -ne $c) { Set-Content -LiteralPath $_.FullName -Value $n -Encoding UTF8 -NoNewline }
}

Write-Host "Done."
