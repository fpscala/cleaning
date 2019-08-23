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
    afterTitle: []
    newArray: []

  vm.getPriceList = ->
    $.ajax
      url: apiUrl.urlName
      type: 'GET'
    .fail handleError
    .done (response) ->
      for arr in response
        vm.priceList(arr)
        arr.map (element) ->
          vm.afterTitle.push element.title
      vm.afterTitle (word) ->
        word isnt "Текстильные изделия"
      console.log(vm.afterTitle())


  console.log(vm.afterTitle())

  vm.getPriceList()

  ko.applyBindings {vm}
