(function(){
	Game.controller('fbController', ['ezfb', '$http', '$timeout', '$location', function(ezfb, $http, $timeout, $location){
		var self=this;
		
		self.fbId=0;
		self.preloadedInviteFriends=true;
		self.shared=true;
		self.pageLiked=true;
		
		self.init=function(){
			ezfb.getLoginStatus(function(r){
				if(r.status=='connected'){
					ezfb.Event.subscribe('edge.create', function(res){
						debugger;
					});
					
					ezfb.Event.subscribe('edge.remove', function(res){
						debugger;
					});
					
					$timeout(function(){
						if($location.url().indexOf('plantation')>0 ||
								$location.url().indexOf('growing')>0){
							//show invite friends
							self.preloadedInviteFriends=false;
							
							//show share-like dialog
							$http.post('/checkShared').then(function(res){
								self.shared=res.data.status;
								
//								ezfb.api('/me/likes/789213031099161', function(res){
//									self.pageLiked=res.data.length>0;
//								});
							});
						}
					}, 2000);
				}
			});
		};
		
		self.shareGame=function(){
			ezfb.ui({
				  method: 'share',
				  href: 'https://apps.facebook.com/applegm',
				}, function(response){
					if(!(response.error_code!=undefined &&response.error_code==4201)){
						$http.post('/shareGame').then(function(res){
							self.shared=true;
						});
					}
				});
		};
		
		self.closeShareLike=function(){
			self.shared=true;
			self.pageLiked=true;
		};
		
		self.initDialog=function(val){
			self.preloadedInviteFriends=val;
		};
		
		self.init();
	}])
	
	.controller('fbInviteFriends', ['ezfb', '$http', '$timeout', function(ezfb, $http, $timeout){
		var self=this;
		
		self.friends=new Array();
		self.checkedFriends=new Array();
		self.dummy=0;
		
		self.init=function(){
			ezfb.getLoginStatus(function(res){
				if(res.status=='connected'){
					self.getFriends();
				}
			});
		};
		
		self.checkFriend=function(item){
			var index=self.checkedFriends.indexOf(item);
			if(index==-1){
				self.checkedFriends.push(item);
			}
			else{
				self.checkedFriends.splice(index, 1);
			}
		};
		
		self.inviteFriends=function(){
			ezfb.ui({method: 'apprequests',
			     to: self.checkedFriends.join(),
			     title: 'My Great Invite',
			     message: 'Check out this Awesome App!',
			   }, function(res){
				   _.forEach(self.checkedFriends, function(f){
					  var ind=_.findIndex(self.friends, function(it){
						  return it.id==f;
					  });
					  if(ind>-1){
						  self.friends.splice(ind, 1);
					  }
				   });
				   self.checkedFriends=new Array();
			   });
		};
		
		self.getFriends=function(){
			ezfb.api("/me/invitable_friends", function(res){
				self.friends=res.data;
			});
		};
		
		self.init();
	}])
	
	.controller('fbShareLike', ['ezfb', '$http', '$timeout', function(ezfb, $http, $timeout){
		var self=this;
		
	}]);
	
}());