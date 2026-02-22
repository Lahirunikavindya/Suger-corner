/**
 * Sugar Corner - Cart JavaScript
 * Manages cart items in localStorage for client-side cart
 */

const CART_KEY = 'sugarCornerCart';

function getCart() {
    try {
        const cart = localStorage.getItem(CART_KEY);
        return cart ? JSON.parse(cart) : [];
    } catch (e) {
        return [];
    }
}

function saveCart(cart) {
    localStorage.setItem(CART_KEY, JSON.stringify(cart));
}

function addToCart(productId, quantity, price, name) {
    const cart = getCart();
    const existing = cart.find(item => item.productId === productId);
    if (existing) {
        existing.quantity += quantity;
    } else {
        cart.push({ productId, quantity, price, name });
    }
    saveCart(cart);
}

function removeFromCart(productId) {
    let cart = getCart().filter(item => item.productId !== productId);
    saveCart(cart);
}

function updateCartQuantity(productId, quantity) {
    const cart = getCart();
    const item = cart.find(i => i.productId === productId);
    if (item) {
        item.quantity = Math.max(0, quantity);
        if (item.quantity === 0) removeFromCart(productId);
        else saveCart(cart);
    }
}
