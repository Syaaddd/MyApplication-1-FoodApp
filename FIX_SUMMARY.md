# Fix for Firebase Deserialization Error

## Problem:
```
com.google.firebase.database.DatabaseException: Expected a List while deserializing, but got a class java.util.HashMap
at com.syadd.myapplication_1_foodapp.Repository.OrderRepository$getAllOrders$1.onDataChange(OrderRepository.kt:68)
```

## Root Cause:
Firebase Realtime Database returns data as HashMap/Map objects, but the code was trying to directly deserialize it to a Kotlin data class using `getValue(OrderModel::class.java)`. This doesn't work properly with nested objects like the `orderItems` list.

## Solution Implemented:

### 1. Updated OrderRepository.kt:
- Changed from direct `getValue(OrderModel::class.java)` to manual mapping
- Added helper methods `mapToOrderModel()` and `mapOrderItems()` 
- These methods properly handle the conversion from Firebase Map objects to our data classes

### 2. Added Proper Error Handling:
- Added try-catch blocks around data conversion
- Handle null values gracefully
- Map Long values to Int where needed (Firebase returns numbers as Long)

### 3. Manual Mapping Process:
- Get raw data as Map<String, Any>
- Extract each field individually with type casting
- Handle nested orderItems separately
- Construct OrderModel and OrderItemModel objects manually

## Files Modified:
- `app/src/main/java/com/syadd/myapplication_1_foodapp/Repository/OrderRepository.kt`

## Functions Updated:
- `getAllOrders()` - now properly handles Firebase data deserialization
- `getUserOrders()` - now properly handles Firebase data deserialization
- Added helper methods for mapping Firebase data to model classes

This fix resolves the crash and allows the app to properly read order data from Firebase Realtime Database.