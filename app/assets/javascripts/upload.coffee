$ ->
  window.Glob ?= {}
#  apiUrl =

  handleError = (error) ->
      alert('something went wrong')

  vm = ko.mapping.fromJS
      # TODO

  vm.onSubmit = ->
#    dataToSend = JSON.stringify(
#      # TODO
#    )
#    $.ajax
#      url: apiUrl.reg
#      type: 'POST'
#      data: dataToSend
#      dataType: 'json'
#      contentType: 'application/json'
#    .fail handleError
#    .done (response) ->
#        alert(response)
#        vm.report()

  ko.applyBindings {vm}
