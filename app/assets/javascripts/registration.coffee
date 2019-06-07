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

  vm.report = ->
    $.ajax
      url: apiUrl.gender
      type: 'GET'
    .fail handleError
    .done (response) ->
      genders = response.gender
      console.log(genders)
      vm.genders(genders)
      console.log(vm.genders())
      console.log(vm.genders().length)

  vm.report()

  vm.option = ->
    $.ajax
      url: apiUrl.education
      type: 'GET'
    .fail handleError
    .done (response) ->
      educations = response.education
      console.log(educations)
      vm.genders(educations)
      console.log(vm.educations())
      console.log(vm.educations().length)

  vm.option()


  ko.applyBindings {vm}
