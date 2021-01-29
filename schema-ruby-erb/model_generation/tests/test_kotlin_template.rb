require_relative 'base_unit_test.rb'
require_relative '../Templates/kotlin_template_helper.rb'
require_relative '../Models/Analytics/touchable_analytics.rb'
require_relative '../Models/Widgets/button.rb'
 
class TestKotlinTemplate < BaseUnitTest

	def setup
    	@kotlinHelper = KotlinTemplateHelper.new
  	end
 
	def test_is_interface_when_interface
		assert_equal(true, @kotlinHelper.is_interface(TouchableAnalytics.new))
	end

	def test_is_interface_when_null
		assert_equal(false, @kotlinHelper.is_interface(nil))
	end

	def test_is_interface_when_widget_android
		assert_equal(true, @kotlinHelper.is_interface(Button.new))
	end
 
end