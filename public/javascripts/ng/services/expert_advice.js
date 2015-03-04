Game.factory("ExpertAdvice", [ '$rootScope','State', function( $rootScope, State) {
	return {
		setAdvice : function(message) {
			if (info!==null && info!=="") {
				var info = {
						'tip':message
				}
				State.set('status',info);
			}
		},
		hide: function() {
			$rootScope.$emit("expert-advice-close");
		},
		setImportantAdvice:function(message) {
			State.set('notification', {
                message: message
              });
		}
	}
}]);