require 'simplecov'

if ENV.fetch('COVERAGE', false)
  SimpleCov.start do
    minimum_coverage 60
  end
end

require "test/unit"

class BaseUnitTest < Test::Unit::TestCase
end