$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    gender: '/get-gender/'
    education: '/get-education'

  handleError = (error) ->
#    vm.isSubmitted(no)
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')


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
