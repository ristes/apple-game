GameDirectives.directive('historyTable', [
  function($) {
    return {
      restrict: 'E',
      scope: {
        history: "=",
        visible: "="
      },
      templateUrl: '/public/templates/history-table.html',

      link: function(scope, element, attrs) {

        scope.hide = function(){
          scope.visible = false;
        }
      }
    };

  }
]);
