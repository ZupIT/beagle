require_relative 'base_unit_test.rb'
require_relative '../Templates/template_helper.rb'
require_relative '../Models/Widgets/button.rb'
require_relative '../Models/Widgets/widget.rb'
 
class TestTemplate < BaseUnitTest

	def setup
    	@helper = TemplateHelper.new
  	end
 
	def test_is_abstract_when_abstract
		assert_equal(true, @helper.is_abstract(Widget.new))
	end

	def test_is_interface_when_null
		assert_equal(false, @helper.is_abstract(nil))
	end

	def test_is_abstract_when_not_abstract
		assert_equal(false, @helper.is_abstract(Button.new))
	end
 
end