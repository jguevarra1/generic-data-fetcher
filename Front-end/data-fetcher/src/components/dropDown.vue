<script>
    export default {
        props: {
            label: String,
            disabled_value: String,
            options: Array,
            modelValue: ''
        },
        methods: {
        updateValue(event) {
            this.$emit('update:modelValue', event.target.value);
        }
    }
    }
</script>

<template>
    <!-- Reusable DropDown component -->
    <div>
        <label class="text-md font-bold text-black p-3">{{label}}</label>
        
        <select :value="modelValue" @input="updateValue" class="w-[90%] mx-3 text-dark z-10 rounded-lg border-gray-300 ">
            <option disabled value="">{{disabled_value}}</option>
            <option class="px-4 py-2 text-sm border-gray-300 rounded-lg bg-gray-50 text-dark hover:bg-gray-100 " v-for="item in this.options">{{item}}</option>
        </select>
    </div>
</template>
