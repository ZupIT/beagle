if which sourcery >/dev/null; then
unset SDKROOT
sourcery --sources Sources --templates Resources/Templates --output Sources/SourceryFiles
else
echo "warning: Sourcery not installed, download using brew install sourcery"
fi
