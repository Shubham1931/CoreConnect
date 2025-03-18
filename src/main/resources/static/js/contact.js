console.log("hello")

const viewContactModal=document.getElementById('view_contact_modal');
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};
const contactModal = new Modal(view_contact_modal,options,instanceOptions);
 
function openContactModal(){
    contactModal.show();
}
function closeContacModal(){
    contactModal.hide();
}
async function loadContactData(contactId) {
  try {
     const data = await (await fetch(`/api/contacts/${contactId}`)) .json();
     document.getElementById("contactName").textContent = data.contactName;
     document.getElementById("contactEmail").textContent = data.contactEmail;
     document.getElementById("contactPhone").textContent = data.contactPhone;
     document.getElementById("contactAddress").textContent = data.address || "n/a";
     document.getElementById("contactImage").src = data.picture;
     const favIcon = document.getElementById("favContactStatus");
        if (data.favrouite) {
            favIcon.innerHTML = "⭐";
            favIcon.classList.remove("hidden"); // Show it
        } else {
            favIcon.classList.add("hidden"); // Hide if not favorite
        }
        const socialElement = document.getElementById("contactSocial");
        if (data.linkedInLink!=null) {
            socialElement.href = data.linkedInLink;
        } else {
            socialElement.href = "#"; // Hide if no profile
        }
    } catch (error) {
    console.log(error);
  }
    
    
  }


function deleteContact(id) {
        console.log("Delete function called for ID:", id); // Debugging

        Swal.fire({
            title: "Are you sure?",
            text: "Delete this Contact",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Yes, delete it!"
        }).then((result) => {
            if (result.isConfirmed) {
                console.log("Deleting Contact with ID:", id);
                window.location.href = "/user/contacts/delete/" + id;
            }
        });
    }
 