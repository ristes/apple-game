Game.controller('NotificationController', ['$scope', 'State', '$modal',
    '$timeout', function($scope, State, $modal, $timeout) {
      var windowTemplateUrl = '/public/templates/notification-dialog.html';

      State.subscribe('notification', 'NotificationController', function(data) {
        $scope.message = data.message || data.exception;
        $scope.duration = data.duration || 5000;
        $scope.title = data.title || "";
        $scope.close = function() {
        	notification.dismiss('cancel');
        }
        var notification = $modal.open({
          backdrop: 'static',
          templateUrl: windowTemplateUrl,
          scope: $scope
        });
        
        $timeout(function() {
          notification.dismiss('cancel');
        }, $scope.duration);
      });

    }]);