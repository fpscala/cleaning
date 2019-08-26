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
    titles: ["Текстильные изделия","Верхняя одежда", "Аксессуары", "Столовое белье", "Покраска", "Постельные принадлежности", "Предметы интерьера", "Прочее"]

  getPriceList = ->
    $.ajax
      url: apiUrl.urlName
      type: 'GET'
    .fail handleError
    .done (response) ->
      vm.priceList response

  getPriceList()


  ko.applyBindings {vm}
