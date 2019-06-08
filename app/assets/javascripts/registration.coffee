$ ->
  window.Glob ?= {}
  apiUrl =
    gender: '/get-gender/'
    education: '/get-education'

  handleError = (error) ->
    alert('something went wrong')

  vm = ko.mapping.fromJS
    genders: []
    educations: []

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

  ko.applyBindings {vm}
