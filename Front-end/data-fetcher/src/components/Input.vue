<script>
    export default {
        props: {
            label: String,
            modelValue: String,
            placeholder: String,
            inputType: String,
            maxLength: Number,
        },
        methods : {
            /**
             * Determine whether or not the input field is optional or not based on its label
             */
            determineOptional(str) {
                if ((str === 'Selector') || (str === 'Headers')) {
                    return '(optional)'
                }
            }
        }
    }
</script>

<template>
    <!-- Reusable input component. Used to generate dynamic form -->
    <div>
        <label class="text-md font-bold text-red-black p-3">{{label}} <span class="italic text-sm"> {{determineOptional(label)}}</span></label>

        <input min='0' :maxlength="this.maxLength" :type="inputType === null ? 'text' : inputType" :value="modelValue" @input="$emit('update:modelValue', $event.target.value)"
        class="w-[90%] border-2 border-gray-500 text-black text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 p-2.5 mx-3 mb-2"
        :placeholder="placeholder">
    </div>
</template>