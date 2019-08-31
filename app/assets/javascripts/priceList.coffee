$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    urlName: '/get-prices'

  handleError = (error) ->
#    vm.isSubmitted(no)
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm = ko.mapping.fromJS
    priceList: []
    titles: []

  getPriceList = ->
    $.ajax
      url: apiUrl.urlName
      type: 'GET'
    .fail handleError
    .done (response) ->
      vm.priceList response
      for k,v of vm.priceList()
        vm.titles.push k

  getPriceList()


  ko.applyBindings {vm}
