Game.factory('GameHttpInterceptors', ['$timeout', 'State',
    function($timeout, State) {

      var windowTemplateUrl = '/public/templates/notification-dialog.html';

      return {
        'responseError': function(response) {
          if (response.status == 401) {
            window.location = "/Crafty/login";
          }
          if (response.status == 400) {
            if (response.data) {
              var data = response.data;
              State.set('notification', {
                exception: data.exception,
                message: data.message
              });
            }
          }
        },
        'response': function(response) {
          if (response.data && response.data.farmer) {
            State.set('farmer', response.data.farmer);
          }
          return response;
        }
      };
    }]);