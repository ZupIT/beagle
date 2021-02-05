if ENV.fetch('COVERAGE', false)
	require 'simplecov'
  	SimpleCov.start

  	if ENV.fetch('CODECOV_TOKEN', false)
  		require 'codecov'
  		SimpleCov.formatter = SimpleCov::Formatter::Codecov
  	end
end

require "test/unit"

class BaseUnitTest < Test::Unit::TestCase
end