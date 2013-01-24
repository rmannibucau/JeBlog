// routing + services
JeBlog = angular.module('JeBlog', [])
.config(['$routeProvider', '$locationProvider', '$httpProvider', function ($routeProvider, $locationProvider, $httpProvider) {
    $routeProvider
        .when('/login', { templateUrl: 'partials/login.html', controller: LoginController })
        .when('/logout', { templateUrl: 'partials/waiting.html', controller: LogoutController  })
        .when('/', { templateUrl: 'partials/posts.html',   controller: PostsController })
        .when('/posts/create', { templateUrl: 'partials/create-post.html', controller: CreatePostController }) // need to be before next one
        .when('/posts/:postId', { templateUrl: 'partials/post.html', controller: PostController })
        .otherwise({ redirectTo: '/' });

        $httpProvider.defaults.transformRequest = function(data){
          return data != undefined ? $.param(data) : null;
        }
}]).factory('storage', function () {
    return {
        contains: function (key) {
            return localStorage.getItem(key) != null;
        },
        get: function(key, defaultValue) {
            return localStorage.getItem(key) || defaultValue;
        },
        set: function(key, value) {
            localStorage.setItem(key, value);
        },
        reset: function() {
            localStorage.clear();
        }
    };
}).factory('header', function () {
    return {
        update: function($scope, storage) {
            $scope.username = storage.get('jeblog#username', 'Anonymous');
            $scope.logged = storage.contains('jeblog#username');
        }
    };
}).factory('users', function () {
    return {
        updateLog: function ($rootScope, $location, newValue) {
            $location.path("/");
            $rootScope.$broadcast('jeblog#username#update', newValue);
        },
        login: function ($rootScope, $location, storage, name) {
            storage.set('jeblog#username', name);
            this.updateLog($rootScope, $location, name);
        },
        logout: function ($rootScope, $location, storage) {
            storage.reset();
            this.updateLog($rootScope, $location, null);
        }
    };
});

// controllers
function CreatePostController($scope, $http, $location) {
    $scope.createPost = function() {
        $http.post('api/post/create', { "title": $scope.title, "content": $scope.content },
            {
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
            })
            .success(function(data) {
                $location.path("/");
            })
            .error(function(data){
                alert('ko');
            });
    }
}

function HeaderController($scope, storage, header) {
    header.update($scope, storage);
    $scope.$on('jeblog#username#update',function (event, data) {
        header.update($scope, storage);
    });
}

function LoginController($rootScope, $scope, $http, $location, storage, users) {
    $scope.success = false;
    $scope.message = null;

    $scope.doLogin = function() {
        $http.post('api/user/login', { "username": $scope.username, "password": $scope.password },
            {
                headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
            })
            .success(function(data) {
                users.login($rootScope, $location, storage, $scope.username);
            })
            .error(function(data){
                $scope.message = "Username or password incorrect.";
                users.updateLog($rootScope, $location, null);
            });
    }
}

function LogoutController($rootScope, $http, $location, storage, users) {
    $http.head('api/user/logout')
                .success(function(data) {
                    users.logout($rootScope, $location, storage);
                })
                .error(function(data){
                    users.logout($rootScope, $location, storage);
                });
}

function PostsController($scope, $http) {
  $scope.posts = $http.get('api/post/list?status=DRAFT') // TODO: use PUBLISHED status
                        .success(function(data) {
                            $scope.posts = data.post;
                        });
}

function PostController($scope, $http, $routeParams) {
  $scope.posts = $http.get('api/post/' + $routeParams.postId)
                        .success(function(data) {
                            $scope.posts = data.post;
                        });
}
