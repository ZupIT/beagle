if which sourcery >/dev/null; then
unset SDKROOT
sourcery --sources BeagleDemo --templates $SRCROOT/../../Sources/BeagleUI/Resources/Templates --output SourceryFiles
else
echo "warning: Sourcery not installed, download using brew install sourcery"
fi
