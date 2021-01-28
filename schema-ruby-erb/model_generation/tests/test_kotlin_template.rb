require_relative '../Templates/kotlin_template_helper.rb'
require_relative '../Models/Analytics/touchable_analytics.rb'
require "test/unit"
 
class TestKotlinTemplate < Test::Unit::TestCase

	def setup
    	@kotlinHelper = KotlinTemplateHelper.new
  	end
 
	def test_isInterface
		assert_equal(true, @kotlinHelper.is_interface(TouchableAnalytics.new))
	end
 
end