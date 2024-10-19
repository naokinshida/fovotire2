function addFavorite(storeId) {
    fetch(`/favorites/${storeId}/add`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
        },
        credentials: 'same-origin',
    })
    .then(response => {
        if (response.status === 409) {
            // 409が返ってきたらモーダルを表示する
            $('#favoriteModal').modal('show');
        } else if (response.ok) {
            alert("お気に入りに追加されました");
        } else {
            alert("エラーが発生しました");
        }
    })
    .catch(error => console.error('Error:', error));
}
