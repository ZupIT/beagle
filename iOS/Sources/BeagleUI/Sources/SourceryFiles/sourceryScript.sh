echo " — — — — — — — — — — test run script started — — — — — — — — — — "
echo " — — — — — — — — — — test run script stopped — — — — — — — — — — "
if which sourcery >/dev/null; then
swiftlint
else
echo "warning: SwiftLint not installed, download from https://github.com/realm/SwiftLint"
fi
