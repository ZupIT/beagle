#!/bin/sh

echo "checking for homebrew updates";
brew update

function install_current {
  echo "trying to update $1"

  brew upgrade $1 || brew install $1 || true
  brew link $1
}

if [ -e "Mintfile" ]; then
  install_current mint
  mint bootstrap
fi

# Install gems if a Gemfile exists
if [ -e "Gemfile" ]; then
  echo "installing ruby gems";

  # install bundler gem for ruby dependency management
  gem install bundler --no-document || echo "failed to install bundle";
  bundle config set deployment 'true';
  bundle install || echo "failed to install bundle";
fi