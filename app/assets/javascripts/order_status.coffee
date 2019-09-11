$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    sendCode: '/get-details'

  vm = ko.mapping.fromJS
    subscribeCode: ''
    detailsCustomer: ''
    customerName: ''
    customerSurName: ''
    customerEmail: ''
    customerPhone: ''
    customerAddress: ''
    customerTypeCleaning: ''
    customerPrice: ''
    customerLinkCode: ''
    customerOrderDay: ''

  handleError = (error) ->
#    vm.isSubmitted(no)
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  $derailsLinkCode = $('#details-costumer')
  vm.showDivDetails = (msg) ->
    console.log(msg)
#    vm.detailsCustomer(msg)
    vm.customerName(msg.firstName)
    vm.customerSurName(msg.surname)
    vm.customerEmail(msg.email)
    vm.customerPhone(msg.phone)
    vm.customerAddress(msg.address)
    vm.customerTypeCleaning(msg.type1)
    vm.customerPrice(msg.price)
    vm.customerLinkCode(msg.linkCode)
    vm.customerOrderDay(msg.orderDay)
#    $derailsLinkCode.show

  vm.subscribeCode.subscribe (code) ->
    vm.customerLinkCode('')
    vm.detailsCustomer ('')
    data =
      linkCode: code

    $.ajax
      url: apiUrl.sendCode
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      if  response is null
        vm.detailsCustomer ("NOT FOUND")
      else
        vm.showDivDetails(response)




  ko.applyBindings {vm}
