import { createWebHistory, createRouter, viewDepthKey } from 'vue-router';
import Home from "../views/Home.vue";
import NotFound from "../components/NotFound.vue"
import Status from "../components/Status.vue"
import ConfigInfo from "../components/ConfigInfo.vue"
import NewConfig from "../components/NewConfig.vue"
import AllJobs from "../components/AllJobs.vue"

const routes = [
    {
        path: "/",
        name: Home,
        component: Home
    },
    {
        path: '/status/:id',
        name: Status,
        component: Status,
    },
    {
        path: '/status/:id/:formID',
        name: ConfigInfo,
        component: ConfigInfo
    },
    {
        path: '/:catchAll(.*)',
        name: NotFound,
        component: NotFound,
    },
    {
        path: '/newConfig',
        name: NewConfig,
        component: NewConfig
    },
    {
        path: '/all',
        name: AllJobs,
        component: AllJobs
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router;