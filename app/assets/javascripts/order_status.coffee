$ ->
  my.initAjax()

  Glob = window.Glob || {}

  vm = ko.mapping.fromJS
    subscribeCode: ''
    textLinkCode:'dad'


  handleError = (error) ->
#    vm.isSubmitted(no)
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')


#  vm.subscribeCode.subscribe (code) ->
#        vm.textLinkCode(code)
#    if pr is undefined
#      vm.textLinkCode('0')
#    else
#      vm.textLinkCode()


  ko.applyBindings {vm}
