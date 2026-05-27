import {createRouter, createWebHistory} from 'vue-router'
import FrontLayout from "@/views/layout/FrontLayout.vue";
import AdminLayout from "@/views/layout/AdminLayout.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: getRoutes()
})

function getRoutes() {
    let defaultRoutes = [
        {
            path: '/',
            name: 'front',
            component: FrontLayout,
            redirect: "/index",
            children: [
                {
                    path: "index",
                    name: "index",
                    component: () => import('../views/front/Index.vue')
                },
                {
                    path: "editCurrentUser",
                    name: "editCurrentUser",
                    component: () => import('../views/EditCurrentUser.vue')
                },
                {
                    path: "editPassword",
                    name: "front-editPassword",
                    component: () => import('../views/EditPassword.vue')
                },
                {
                    path: "balanceInfo",
                    name: "front-balanceInfo",
                    component: () => import('../views/BalanceInfo.vue')
                },
                {
                    path: 'bookList',
                    name: 'front-bookList',
                    component: () => import('../views/front/BookList.vue')
                },
                {
                    path: 'bookDetails/:id',
                    name: 'front-bookDetails',
                    component: () => import('../views/front/BookDetails.vue')
                },
                {
                    path: 'bookOrder',
                    name: 'front-bookOrder',
                    component: () => import('../views/front/BookOrder.vue')
                },
                {
                    path: 'shoppingCart',
                    name: 'front-shoppingCart',
                    component: () => import('../views/front/ShoppingCart.vue')
                },
                {
                    path: 'personalCenter',
                    name: 'front-personalCenter',
                    component: () => import('../views/front/PersonalCenter.vue')
                },
            ]
        },
        {
            path: '/admin',
            name: 'admin',
            component: AdminLayout,
            redirect: "/admin/home",
            children: [
                {
                    path: "home",
                    name: "admin-home",
                    component: () => import('../views/admin/Home.vue')
                },
                {
                    path: 'editCurrentUser',
                    name: 'admin-editCurrentUser',
                    component: () => import('../views/EditCurrentUser.vue')
                },
                {
                    path: 'editPassword',
                    name: 'admin-editPassword',
                    component: () => import('../views/EditPassword.vue')
                },
                {
                    path: 'admin',
                    name: 'Admin',
                    component: () => import('../views/admin/AdminManage.vue')
                },
                {
                    path: 'user',
                    name: 'admin-user',
                    component: () => import('../views/admin/UserManage.vue')
                },
                {
                    path: 'shop',
                    name: 'admin-shop',
                    component: () => import('../views/admin/ShopManage.vue')
                },
                {
                    path: 'bookCategory',
                    name: 'admin-bookCategory',
                    component: () => import('../views/admin/BookCategoryManage.vue')
                },
                {
                    path: 'book',
                    name: 'admin-book',
                    component: () => import('../views/admin/BookManage.vue')
                },
                {
                    path: 'bookCollect',
                    name: 'admin-bookCollect',
                    component: () => import('../views/admin/BookCollectManage.vue')
                },
                {
                    path: 'browsingHistory',
                    name: 'admin-browsingHistory',
                    component: () => import('../views/admin/BrowsingHistoryManage.vue')
                },
                {
                    path: 'shippingAddress',
                    name: 'admin-shippingAddress',
                    component: () => import('../views/admin/ShippingAddressManage.vue')
                },
                {
                    path: 'bookOrder',
                    name: 'admin-bookOrder',
                    component: () => import('../views/admin/BookOrderManage.vue')
                },
                {
                    path: 'orderEvaluate',
                    name: 'admin-orderEvaluate',
                    component: () => import('../views/admin/OrderEvaluateManage.vue')
                },
                {
                    path: 'shopCollect',
                    name: 'admin-shopCollect',
                    component: () => import('../views/admin/ShopCollectManage.vue')
                },
                {
                    path: 'slideshow',
                    name: 'admin-slideshow',
                    component: () => import('../views/admin/SlideshowManage.vue')
                },
                {
                    path: 'announcement',
                    name: 'admin-announcement',
                    component: () => import('../views/admin/AnnouncementManage.vue')
                },
                {
                    path: 'message',
                    name: 'admin-message',
                    component: () => import('../views/admin/MessageManage.vue')
                },
            ]
        },
        {
            path: "/login",
            name: "login",
            component: () => import('../views/Login.vue')
        },
        {
            path: "/register",
            name: "register",
            component: () => import('../views/Register.vue')
        }
    ];
    defaultRoutes.push({
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        meta: { name: '' },
        component: () => import('../views/404.vue')
    })
    return defaultRoutes;
}

router.beforeEach((to, from, next) => {
    next();
});
export default router
