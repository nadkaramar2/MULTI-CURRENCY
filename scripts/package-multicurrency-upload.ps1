# Package MULTI-CURRENCY source for AWS upload (no build artifacts).
$ErrorActionPreference = "Stop"
$Root = "C:\MULTI-CURRENCY1"
$Staging = Join-Path $env:TEMP "MultiCurrency-upload-staging"
$OutZip = Join-Path $Root "MultiCurrency.zip"

if (Test-Path $Staging) { Remove-Item $Staging -Recurse -Force }
New-Item -ItemType Directory -Path $Staging | Out-Null

$excludeDirs = @("node_modules", "target", "build", ".git", "__pycache__", ".metadata")

function Copy-ProjectTree {
    param([string]$Source, [string]$Dest)
    $xd = ($excludeDirs | ForEach-Object { "/XD", $_ }) -join " "
    $null = cmd /c "robocopy `"$Source`" `"$Dest`" /E /NFL /NDL /NJH /NJS /nc /ns /np $xd /XF *.jar *.class *.log"
    if ($LASTEXITCODE -ge 8) { throw "robocopy failed for $Source (exit $LASTEXITCODE)" }
}

$backendSrc = Join-Path $Root "MultiCurrencyCodeBackup\CMSAccountManagementService_app"
$uiSrc = Join-Path $Root "ui\Multcurrency_updatedCode-31-10-2024"
$sqlSrc = Join-Path $Root "MultiCurrencyCodeBackup\Multicurrency_database_20-07-2024.sql"

if (-not (Test-Path $backendSrc)) { throw "Backend not found: $backendSrc" }
if (-not (Test-Path $uiSrc)) { throw "Frontend not found: $uiSrc" }

Copy-ProjectTree $backendSrc (Join-Path $Staging "backend\CMSAccountManagementService_app")
Copy-ProjectTree $uiSrc (Join-Path $Staging "ui\Multcurrency_updatedCode-31-10-2024")
if (Test-Path $sqlSrc) {
    New-Item -ItemType Directory -Path (Join-Path $Staging "DB") -Force | Out-Null
    Copy-Item $sqlSrc (Join-Path $Staging "DB\Multicurrency_database_20-07-2024.sql")
}

if (Test-Path $OutZip) { Remove-Item $OutZip -Force }
# tar produces Linux-friendly paths and directory permissions (Compress-Archive can break unzip on EC2)
Push-Location $Staging
tar -caf $OutZip .
Pop-Location
Remove-Item $Staging -Recurse -Force

$mb = [math]::Round((Get-Item $OutZip).Length / 1MB, 2)
Write-Host "Created $OutZip ($mb MB)"
