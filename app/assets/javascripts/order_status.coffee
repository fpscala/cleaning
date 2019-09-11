$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    sendCode: '/get-details'

  customerData =
    customerName: ''
    customerSurName: ''
    customerEmail: ''
    customerPhone: ''
    customerAddress: ''
    customerTypeCleaning: ''
    customerStatusOrder: ''
    customerPrice: ''
    customerOrderDay: ''

  vm = ko.mapping.fromJS
    subscribeCode: ''
    detailsCustomer: ''
    customerLinkCode: ''
    details: customerData

  handleError = (error) ->
#    vm.isSubmitted(no)
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.showDivDetails = (msg) ->
    console.log(msg)
    vm.details.customerName(msg.firstName)
    vm.details.customerStatusOrder(msg.statusOrder)
    vm.details.customerSurName(msg.surname)
    vm.details.customerEmail(msg.email)
    vm.details.customerPhone(msg.phone)
    vm.details.customerAddress(msg.address)
    vm.details.customerTypeCleaning(msg.type1)
    vm.details.customerPrice(msg.price)
    vm.details.customerOrderDay(msg.orderDay)
    vm.customerLinkCode(msg.linkCode)

  vm.subscribeCode.subscribe (code) ->
    vm.customerLinkCode('')
    vm.detailsCustomer('')

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
        vm.detailsCustomer("NOT FOUND")
      else
        vm.showDivDetails(response)




  ko.applyBindings {vm}
