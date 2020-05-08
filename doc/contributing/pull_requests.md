# Beagle Pull Request Submission Guidelines:

1. Search [GitHub](https://github.com/ZupIT/beagle/pulls) for an open or closed PR
  that relates to your submission. You don't want to duplicate effort.
1. Be sure that an issue describes the problem you're fixing, or documents the design for the feature you'd like to add.
  Discussing the design up front helps to ensure that we're ready to accept your work.
1. Fork the ZupIT/beagle repo.
1. Make your changes in a new git branch:

     ```shell
     git checkout -b my-fix-branch master
     ```

1. Create your patch, **including appropriate test cases**.
1. Follow our [Coding Rules](https://github.com/ZupIT/beagle/blob/master/doc/contributing/coding_rules.md).
1. Commit your changes using a descriptive commit message that follows our
  [commit message conventions](https://github.com/ZupIT/beagle/blob/master/doc/contributing/commits.md). Adherence to these conventions
  is necessary because release notes are automatically generated from these messages.

     ```shell
     git commit -a
     ```
    Note: the optional commit `-a` command line option will automatically "add" and "rm" edited files.

1. Push your branch to GitHub:

    ```shell
    git push origin my-fix-branch
    ```

1. In GitHub, send a pull request to `beagle:master`.
* If we suggest changes then:
  * Make the required updates.
  * Re-run the Beagle test suites to ensure tests are still passing.
  * Rebase your branch and force push to your GitHub repository (this will update your Pull Request):

    ```shell
    git rebase master -i
    git push -f
    ```

1. Title PR.

    We use conventional commits specifications to write meaningful PR titles that are used as part of our [semantic release](#Wiki) process.

    Please read the official specifications for more details: https://www.conventionalcommits.org/.

    ## What types can I use for my PR title?

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

    For example:

    ```
        feat: add new field in component image
    ```

    > **Note:**
    > If you're introducing a breaking change, the message body should start with [`BREAKING CHANGE:`](https://www.conventionalcommits.org/en/v1.0.0/#commit-message-with-description-and-breaking-change-footer),

That's it! Thank you for your contribution!

#### After your pull request is merged

After your pull request is merged, you can safely delete your branch and pull the changes
from the main (upstream) repository:

* Delete the remote branch on GitHub either through the GitHub web UI or your local shell as follows:

    ```shell
    git push origin --delete my-fix-branch
    ```

* Check out the master branch:

    ```shell
    git checkout master -f
    ```

* Delete the local branch:

    ```shell
    git branch -D my-fix-branch
    ```

* Update your master with the latest upstream version:

    ```shell
    git pull --ff upstream master
    ```

# Wiki

Why are we using semantic-release and how does it work?
We chose to use semantic-release to handle our process publishing new release because it was the most widely used tool for publishing and gave easy support for managing changelogs.