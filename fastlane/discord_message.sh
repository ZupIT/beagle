curl -XPOST -H "Content-type: application/json" -d '{
  "username": "Beagle Bot",
  "embeds": [
    {
      "fields": [
        {
          "name": "Release",
          "value": "'"$VERSION_DEPLOY"'",
          "inline": true
        },
        {
          "name": "Change log of this release",
          "value": "https://github.com/ZupIT/beagle/releases/tag/'"$VERSION_DEPLOY"'"
        }
      ],
      "thumbnail": {
        "url": "https://media-exp1.licdn.com/dms/image/C4E0BAQFB_zNdQWh9zg/company-logo_200_200/0?e=1595462400&v=beta&t=bRX7ZjGDz5krIyBvXe1qw8raZdjU-4v1GuZ03EgYSqk"
      }
    }
  ]
}' $DISCORD_WEBHOOK