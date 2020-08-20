# Beagle Pull Request Submission Guidelines

1. Search for issues and PRs that relates to your submission. You don't want to duplicate effort.
1. Be sure that an issue describes the problem you're fixing, or documents the design for the feature you'd like to add. Discussing the design up front helps to ensure that we're ready to accept your work.
1. Fork the ZupIT/beagle repo.
1. Make your changes in a new git branch.
1. Follow our [Coding Rules](https://github.com/ZupIT/beagle/blob/master/doc/contributing/coding_rules.md).
1. Commit your changes using a descriptive commit message, and make sure to **include appropriate test cases**.
1. Push your branch to GitHub.
1. In GitHub, send a pull request to `beagle:master`.

## After Submitting Pull Request

### Choosing a correct Title

They should look like this:

```txt
feat: add field in component image to allow resizing
```

This is a format called **conventional commits**. It helps us understand what each modification is actually doing, and allow us to automatically generate pretty *release notes* and *versions*.

Please read the [official specifications](https://www.conventionalcommits.org/) for more details.

#### What types can I use for my PR title?

By "types" we mean the prefix of a commit message like `feat:`, and they can be one of the following values:

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
- **chore:** other changes that don't modify source or test files. Use this type when adding or
  updating dev dependencies.
- **revert:** reverts a previous commit.

> **Note:**
> If you're introducing a breaking change, the message body should start with [`BREAKING CHANGE:`](https://www.conventionalcommits.org/en/v1.0.0/#commit-message-with-description-and-breaking-change-footer)

### Following changes

After we analyse your contibution, we may comment some questions and suggestions, make sure to:

- Consider making the required updates.
- Re-run the Beagle test suites to ensure tests are still passing.
- Rebase your branch and force push to your GitHub repository (this will update your Pull Request):

  ```shell
  git rebase master -i
  git push -f
  ```

----

**That's it! Thank you for your contribution! 😁**
