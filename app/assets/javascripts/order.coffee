$ ->
  window.Glob ?= {}
  apiUrl =
    prices: '/get-prices'
    counts: '/get-counts'

  handleError = (error) ->
    alert('something went wrong')

  vm = ko.mapping.fromJS
    prices: []
    counts: []

  vm.getPrices = ->
    $.ajax
      url: apiUrl.prices
      type: 'GET'
    .fail handleError
    .done (response) ->
      for price in response
        vm.prices(price)
    $.ajax
      url: apiUrl.counts
      type: 'GET'
    .fail handleError
    .done (response) ->
      for count in response
        vm.counts(count)

  vm.getPrices()

  ko.applyBindings {vm}
