$ ->
  window.Glob ?= {}
  apiUrl =
    genders: '/get-gender/'

  handleError = (error) ->
    alert('something went wrong')

  vm = ko.mapping.fromJS
    genders: []

  vm.report = ->
    $.ajax
      url: apiUrl.genders
      type: 'GET'
    .fail handleError
    .done (response) ->
      genders = response.gender
      console.log(genders)
      vm.genders(genders)
      console.log(vm.genders())
      console.log(vm.genders().length)

  vm.report()


  ko.applyBindings {vm}
