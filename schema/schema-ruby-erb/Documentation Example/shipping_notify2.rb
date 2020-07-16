# downloaded from https://www.druby.org/sidruby/3-1-generating-templates-with-erb.html
require 'erb'
require 'date'

class ShippingNotify
  def initialize
    @account = ''
    @customer = ''
    @items = []
    @erb = ERB.new(File.read('shipping_notify.erb')) 
  end
  attr_accessor :account, :customer, :items

  def to_s
    @erb.result(binding)
  end
end
if __FILE__ == $0
  greetings = ShippingNotify.new

  greetings.account = 'm_seki'
  greetings.customer = 'Masatoshi SEKI'
  items = [[1, 'Recollections of erb', Date.new(2008, 6, 22)], 
           [1, 'Great BigTable and my toys', Date.new(2009, 7, 18)],
           [1, 'The last decade of RWiki and lazy me',  Date.today]]
  greetings.items = items

  puts greetings.to_s
end



