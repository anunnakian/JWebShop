(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('category-page', {
            parent: 'app',
            url: '/category/{id}',
            data: {
                authorities: [],
                pageTitle: 'jWebShopApp.category.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'CategoryPageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('product');
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }],
                entities: ['$stateParams', 'ProductCategory', function($stateParams, ProductCategory) {
                    return ProductCategory.query({id : $stateParams.id}).$promise;
                }]
            }
        });
    }
})();
