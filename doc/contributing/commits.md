# Beagle commit conventions

We use conventional commits specifications to write meaningful commit messages that are used as part of our [semantic release](#Wiki) process.

Please read the official specifications for more details: https://www.conventionalcommits.org/.

Each commit message consists of a **header**, a **body**, and a **footer**. The header has a special
format that includes a **type**, a **scope**, and a **subject**:

```plaintext
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

The header is mandatory and the scope of the footer is optional.
Each line in the commit message should be no longer than 100 characters.

When opening an PR, make sure you do at least one of the following:

- Include at least one commit message that complies with the conventional commit standards.
- Ensure the PR's title itself complies with the conventional commit standards.

## Is it okay that all my commits don't follow the conventions in a single PR?

It's recommended that all commits follow the conventions because we refer to those commits when generating changelogs.

```
34d2331 Correcting something in the awesome feature
13131da feat: adding an awesome feature
```

When generating the changelog for the above, we will reference commit `13131da` which follows the conventions, but looking at the diff
for this commit will not give a complete overview of the feature it describes, which might be confusing. Ideally, commit `34d2331` should
have been squashed in `13131da`.

It's okay to not always follow the recommendation above, as long as every meaningful change is described by one properly formatted message.
Example:

```
34d2331 Apply review suggestion
13131da feat: adding an awesome feature
```

In the example above, you might want to keep `13131da` and `34d2331` separated to help in the review process and that would be fine.

> **Note:** It would NOT be all right for `34d2331` to follow the conventions because it doesn't bring any meaningful change to `master`.
** Conventional commits should only be used to describe changes that will land in the main branch, NOT for changes to your own branch.**

## What types can I use for my commit messages?

- **feat:** a new feature (adding a new component, providing new variants for an existing component, etc.).
- **fix:** a bug fix (correcting a styling issue, addressing a bug in a component's API, etc.).
  When updating non-dev dependencies, mark your changes with the `fix:` type.
- **docs:** documentation-only changes.
- **style:** changes that do not affect the meaning of the code
  (whitespace, formatting, missing semicolons, etc). _Not_ to be used for CSS changes as those are
  meaningful changes, consider using `feat:` of `fix:` instead.
- **refactor:** a code change that neither fixes a bug nor adds a feature.
- **perf:** a code change that improves performance.
- **test:** adding missing tests or correcting existing tests.
- **build:** changes that affect the build system (changing webpack or Rollup config for example).
- **ci:** changes to our CI configuration files and scripts
  (changing `Fastlane`, adding or changing Danger plugins, etc.).
- **chore:** Other changes that don't modify source or test files. Use this type when adding or
  updating dev dependencies.
- **revert:** reverts a previous commit.

For example:

```
feat: add new field in component image
```

> **Note:**
> If you're introducing a breaking change, the message body should start with [`BREAKING CHANGE:`](https://www.conventionalcommits.org/en/v1.0.0/#commit-message-with-description-and-breaking-change-footer),


# Wiki

Why are we using semantic-release and how does it work?
We chose to use semantic-release to handle our process publishing new release because it was the most widely used tool for publishing and gave easy support for managing changelogs.
