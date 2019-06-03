$ ->
  window.Glob ?= {}
  apiUrl =
    reg: '/register/'

  handleError = (error) ->
      alert('something went wrong')

  vm = ko.mapping.fromJS
    email:'eee@qwe'
    psw: ''
    comment: ''
    slanguages: []
    planguage: ''
    users: []
    city: ''
    country: ''
    countries: []

  vm.onSubmit = ->
    dataToSend = JSON.stringify(
      email: vm.email()
      psw: vm.psw()
      comment: vm.comment()
      sLanguages: vm.slanguages()
      pLanguage: vm.planguage()
    )
    $.ajax
      url: apiUrl.reg
      type: 'POST'
      data: dataToSend
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
        alert(response)

  ko.applyBindings {vm}
