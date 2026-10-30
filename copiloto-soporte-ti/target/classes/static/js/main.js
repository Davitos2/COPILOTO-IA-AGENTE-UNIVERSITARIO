document.addEventListener('DOMContentLoaded', () => {
    // 1. Obtener la sesión del usuario guardada en el navegador
    const usuarioSesion = JSON.parse(localStorage.getItem('usuarioSesion'));

    // Si no hay sesión iniciada, fuerza el login
    if (!usuarioSesion) {
        window.location.href = 'login.html';
        return;
    }

    // 2. Mostrar datos del usuario autenticado en la barra superior
    const labelUsuario = document.getElementById('nombreUsuarioActivo');
    if (labelUsuario) {
        const rolNombre = usuarioSesion.idRol === 1 ? 'Estudiante' : (usuarioSesion.idRol === 2 ? 'Analista TI' : 'Administrador TI');
        labelUsuario.innerHTML = `<i class="bi bi-person-circle"></i> ${usuarioSesion.nombre} <span class="badge bg-secondary ms-1">${rolNombre}</span>`;
    }

    // 3. Evaluar Rol e intercambiar pantallas
    const vistaEstudiante = document.getElementById('vistaEstudiante');
    const vistaAdmin = document.getElementById('vistaAdmin');

    if (usuarioSesion.idRol === 1) {
        // ESTUDIANTE (IdRol = 1): Ve únicamente la pantalla de registro
        if (vistaEstudiante) vistaEstudiante.classList.remove('d-none');
        if (vistaAdmin) vistaAdmin.classList.add('d-none');

        if (document.getElementById('nombreSolicitante')) {
            document.getElementById('nombreSolicitante').value = usuarioSesion.nombre;
            document.getElementById('correoSolicitante').value = usuarioSesion.correo;
        }
    } else {
        // ANALISTA O ADMIN (IdRol = 2 o 3): Ve únicamente la Supervisión de Tickets
        if (vistaEstudiante) vistaEstudiante.classList.add('d-none');
        if (vistaAdmin) vistaAdmin.classList.remove('d-none');

        // Cargar lista de tickets pendientes de respuesta de IA
        cargarTicketsSupervision();
    }

    // 4. Procesar formulario de envío de tickets
    const ticketForm = document.getElementById('ticketForm');
    if (ticketForm) {
        ticketForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            const ticketData = {
                idUsuarioCreador: usuarioSesion.idUsuario,
                titulo: document.getElementById('titulo').value,
                descripcion: document.getElementById('descripcion').value
            };

            try {
                const response = await fetch('http://localhost:8080/api/tickets', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(ticketData)
                });

                if (response.ok) {
                    const ticketGuardado = await response.json();
                    if (!ticketGuardado.idTicket) {
                        alert("❌ Error: No se pudo guardar en SQL Server.");
                        return;
                    }
                    const msg = document.getElementById('mensajeExito');
                    msg.classList.remove('d-none');
                    ticketForm.reset();
                    setTimeout(() => { msg.classList.add('d-none'); }, 4000);
                } else {
                    alert("Error 500 al procesar el ticket.");
                }
            } catch (error) {
                alert("Error de conexión con el servidor.");
            }
        });
    }
});

// Cargar la lista de tickets para la pantalla de Supervisión (Wireframe 2)
async function cargarTicketsSupervision() {
    const contenedor = document.getElementById('listaTicketsSupervision');
    if (!contenedor) return;

    contenedor.innerHTML = `<div class="col-12 text-center py-5"><div class="spinner-border text-primary"></div><p class="mt-2 text-muted">Cargando tickets...</p></div>`;

    try {
        const response = await fetch('http://localhost:8080/api/tickets');
        if (response.ok) {
            const tickets = await response.json();

            if (tickets.length === 0) {
                contenedor.innerHTML = `<div class="col-12 alert alert-info text-center">No hay tickets registrados en el sistema.</div>`;
                return;
            }

            contenedor.innerHTML = tickets.map(t => `
                <div class="col-12">
                    <div class="card card-custom">
                        <div class="card-header bg-white d-flex justify-content-between align-items-center py-3">
                            <span class="fw-bold text-primary fs-5">Ticket #TKT-${String(t.idTicket).padStart(4, '0')}</span>
                            <div>
                                <span class="badge ${t.prioridad === 'Alta' ? 'bg-danger' : 'bg-warning text-dark'} me-1">${t.prioridad || 'Media'}</span>
                                <span class="badge bg-info text-dark">${t.estado || 'Abierto'}</span>
                            </div>
                        </div>
                        <div class="card-body p-4">
                            <div class="row mb-3">
                                <div class="col-md-8">
                                    <h5 class="fw-bold text-dark">${t.titulo}</h5>
                                    <p class="text-secondary">${t.descripcion}</p>
                                </div>
                                <div class="col-md-4 bg-light p-3 rounded">
                                    <small class="text-muted d-block mb-1"><strong>ID Usuario Solicitante:</strong> ${t.idUsuarioCreador}</small>
                                    <small class="text-muted d-block"><strong>Fecha de Registro:</strong> ${t.fechaCreacion ? t.fechaCreacion.replace('T', ' ').substring(0, 19) : 'Reciente'}</small>
                                </div>
                            </div>

                            <!-- MÓDULO SUPERVISIÓN IA -->
                            <div class="border border-primary-subtle rounded p-3 mb-3 style="background-color: #f8fafd;">
                                <div class="d-flex align-items-center gap-2 mb-2 text-primary fw-semibold">
                                    <i class="bi bi-robot fs-5"></i> Solución Propuesta por la IA
                                </div>
                                <p class="small text-dark mb-0">
                                    <em>El inconveniente reportado puede estar asociado a permisos de cuenta. Se sugiere verificar el acceso desde una ventana de incógnito o reiniciar las credenciales en el directorio activo.</em>
                                </p>
                            </div>

                            <div class="mb-3">
                                <label class="form-label small fw-semibold">Revisión del Supervisor (Comentario opcional):</label>
                                <textarea id="comentario_${t.idTicket}" class="form-control form-control-sm" rows="2" placeholder="Escriba observaciones para el estudiante..."></textarea>
                            </div>

                            <div class="d-flex gap-2 justify-content-end">
                                <button onclick="responderTicket(${t.idTicket}, 'Rechazado')" class="btn btn-outline-danger btn-sm"><i class="bi bi-x-circle"></i> Rechazar</button>
                                <button onclick="responderTicket(${t.idTicket}, 'Aprobado')" class="btn btn-success btn-sm"><i class="bi bi-check-circle"></i> Aprobar y Enviar Respuesta</button>
                            </div>
                        </div>
                    </div>
                </div>
            `).join('');
        }
    } catch (e) {
        contenedor.innerHTML = `<div class="col-12 alert alert-danger">Error al conectar con la API de tickets.</div>`;
    }
}

function responderTicket(idTicket, estado) {
    const comentario = document.getElementById(`comentario_${idTicket}`)?.value || '';
    alert(`Ticket #TKT-${String(idTicket).padStart(4, '0')} fue marcado como [${estado}].\nObservación: ${comentario || 'Sin comentarios'}`);
}

function cerrarSesion() {
    localStorage.removeItem('usuarioSesion');
    window.location.href = 'login.html';
}