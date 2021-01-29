require_relative '../Templates/kotlin_template_helper.rb'
require_relative '../Models/Analytics/touchable_analytics.rb'
# require_relative '../Models/Widgets/button.rb'

require "test/unit"
 
class TestKotlinTemplate < Test::Unit::TestCase

	def setup
    	@kotlinHelper = KotlinTemplateHelper.new
  	end
 
	def test_is_interface
		assert_equal(true, @kotlinHelper.is_interface(TouchableAnalytics.new))
	end

	def test_is_interface_null
		assert_equal(false, @kotlinHelper.is_interface(nil))
	end

	# TODO refactor button imports
	# def test_is_interface_widget_android
	# 	assert_equal(true, @kotlinHelper.is_interface(Button.new))
	# end
 
end