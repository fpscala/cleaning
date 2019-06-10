$ ->
  window.Glob ?= {}
  apiUrl =
    prices: '/get-prices'

  handleError = (error) ->
    alert('something went wrong')

  vm = ko.mapping.fromJS
    prices: []

  vm.getPrices = ->
    $.ajax
      url: apiUrl.prices
      type: 'GET'
    .fail handleError
    .done (response) ->
      for price in response
        vm.prices(price)

  vm.getPrices()

  ko.applyBindings {vm}
