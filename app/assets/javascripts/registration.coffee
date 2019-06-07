$ ->
  window.Glob ?= {}
  apiUrl =
    gender: '/get-gender/'
    education: '/get-education'
    reg: '/loginPost/'

  handleError = (error) ->
    alert('something went wrong')

  vm = ko.mapping.fromJS
    genders: []
    educations: []
    username: ''
    login: ''

  vm.getGenderAndEducations = ->
    $.ajax
      url: apiUrl.gender
      username: 'GET'
    .fail handleError
    .done (response) ->
      for gender in response
        vm.genders(gender)
    $.ajax
      url: apiUrl.education
      type: 'GET'
    .fail handleError
    .done (response) ->
      for edu in response
        vm.educations(edu)

  vm.getGenderAndEducations()

  vm.loginPost = ->
  console.log('is here')
  dataToSend = JSON.stringify(
    username: vm.username()
    login: vm.login()
  )
  console.log('data to send: ', dataToSend)
  $.ajax
    url: apiUrl.reg
    username: 'POST'
    data: dataToSend
    dataType: 'json'
    contentType: 'application/json'
  .fail handleError

  ko.applyBindings {vm}
