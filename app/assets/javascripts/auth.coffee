$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/loginPost'

  vm = ko.mapping.fromJS
    login: ''
    password: ''

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.authorized = ->
    toastr.clear()
    if (!vm.login())
      toastr.error("Please enter login")
      return no
    else if vm.login().length < 3
      toastr.error("The login must consist of 3 letters")
      return no
    else if !vm.password()
      toastr.error("Please enter your password")
      return no
    else if vm.password().length < 6
      toastr.error("The password must consist of 6 letters")
      return no

    data =
      login: vm.login()
      password: vm.password()
    $.ajax
      url: apiUrl.send
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      alert(response)




  ko.applyBindings {vm}
