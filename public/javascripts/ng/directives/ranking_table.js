GameDirectives.directive('rankingTable', [
  function($) {
    return {
      restrict: 'E',
      scope: {
        rankings: "=",
        visible: "="
      },
      templateUrl: '/public/templates/ranking-table.html',

      link: function(scope, element, attrs) {
        scope.displayedRankings = [].concat(scope.rankings);

        scope.hide = function(){
          scope.visible = false;
        }
      }
    };

  }
]);
