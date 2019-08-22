$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    urlName: '/get-names'
    send: '/add-order'

#  Page =
#    Home: 'home'
#    ThankYou: 'thankYou'
#    AlreadySubmitted: 'submitted'

#  defaultPage =
#    if (Glob.alreadySubmitted)
#      Page.AlreadySubmitted
#    else Page.Home


  vm = ko.mapping.fromJS
    surname: ''
    firstName: ''
    email: ''
    phone: ''
    address: ''
    comment: ''
    price:''
    names: []
    name: ''
    selectedProductId: ''
    textLinkCode:''
#    isSubmitted: no
#    page: defaultPage


  vm.getNames = ->
    $.ajax
      url: apiUrl.urlName
      type: 'GET'
    .fail handleError
    .done (response) ->
      for arr in response
        vm.names(arr)

  vm.getNames()

  handleError = (error) ->
    vm.isSubmitted(no)
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')



  vm.selectedProductId.subscribe (pr) ->
    for goods in vm.names()
      if goods.id is pr
        vm.name(goods.name)
        vm.price(goods.price)
    if pr is undefined
      vm.price('0')
    else
      vm.price()

#  vm.Page = Page

  $modalSendCommitment = $('#send-linkCode')
  vm.showModalSendCommitment = (msg) ->
    vm.textLinkCode(msg)
    $modalSendCommitment.modal('show')

  vm.onSubmit = ->
    toastr.clear()
    if (!vm.surname())
      toastr.error("Please enter sur name")
      return no
    else if !vm.firstName()
      toastr.error("Please enter your name")
      return no
    else if !vm.email()
      toastr.error("Please enter your email address")
      return no
    else if !my.isValidEmail(vm.email())
      toastr.error("Please enter valid email address")
      return no
    else if !vm.phone()
      toastr.error("Please enter your phone number")
      return no
    else if (!my.isValidPhone(vm.phone()))
      toastr.error("Please enter  valid phone number")
      return no
    else if !vm.address()
      toastr.error("Please enter your address")
      return no
    else if (vm.price() == '0')
      toastr.error("Please select type goods")
      return no

    data =
      surname: vm.surname()
      firstName: vm.firstName()
      email: vm.email()
      phone: vm.phone()
      address: vm.address()
      typeCleaning: vm.name()
      comment: vm.comment()

    console.log(data)

#    vm.isSubmitted(yes)
    $.ajax
      url: apiUrl.send
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      vm.showModalSendCommitment(response)


  ko.applyBindings {vm}
