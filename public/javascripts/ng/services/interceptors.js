Game.factory('GameHttpInterceptors', ['$timeout', 'State',
    function($timeout, State) {

      var windowTemplateUrl = '/public/templates/notification-dialog.html';

      return {
        'response': function(response) {
          if (response.data && response.data.status === false) {
            var data = response.data;
            State.set('notification', {
              exception: data.exception,
              message: data.message
            });
          }
          return response;
        }
      };
    }]);