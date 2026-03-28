function addFeatureRow() {
    const container = document.getElementById('features-container');

    const row = document.createElement('div');
    row.className = 'row mb-2 feature-row';

    row.innerHTML = `
        <div class="col-5">
            <input type="text" name="featureKeys" class="form-control" placeholder="Nombre (ej. Material)" required>
        </div>
        <div class="col-5">
            <input type="text" name="featureValues" class="form-control" placeholder="Valor (ej. Aluminio)" required>
        </div>
        <div class="col-2">
            <button type="button" class="btn btn-danger w-100" onclick="this.closest('.feature-row').remove()"
                    title="Eliminar fila">
                <i class="bi bi-trash"></i>
            </button>
        </div>
    `
    container.appendChild(row);
}