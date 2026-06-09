// import React, { useState } from "react";

// /*******
//  * @CustomAlertBox  this is custom modal is used for Alert box
//  * @Author by Lalit created at 2023-10-26
//  */

// function CustomAlert({ message, showAlert }) {
//   const [open, setOpen] = useState(showAlert);

//   return (
//     <div
//       className={`fixed top-0 left-0 w-full h-full flex items-center justify-center  bg-gray-600  ${
//         open ? "" : "hidden"
//       }`}
//     >
//       <div className="w-full md:w-1/3 mx-auto rounded-lg shadow-lg ">
//         <div className="bg-blue-900 text-white text-sm font-bold px-4 py-1.5 flex items-center justify-between ">
//           <span className="flex ml-2">
//             <span className="flex px-1">
//               {" "}
//               <svg
//                 xmlns="http://www.w3.org/2000/svg"
//                 fill="none"
//                 viewBox="0 0 24 24"
//                 stroke-width="1.5"
//                 stroke="currentColor"
//                 className="w-6 h-6"
//               >
//                 <path
//                   stroke-linecap="round"
//                   stroke-linejoin="round"
//                   d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z"
//                 />
//               </svg>
//             </span>
//             Alert
//           </span>
//           <button
//             onClick={() => {
//               setOpen(false);
//             }}
//             className="hover:bg-blue-800 hover:text-white focus:ring-4"
//           >
//             <svg
//               xmlns="http://www.w3.org/2000/svg"
//               className="w-6 h-6 fill-current text-white"
//               fill="none"
//               viewBox="0 0 24 24"
//               stroke="currentColor"
//             >
//               <path
//                 strokeLinecap="round"
//                 strokeLinejoin="round"
//                 strokeWidth="2"
//                 d="M6 18L18 6M6 6l12 12"
//               />
//             </svg>
//           </button>
//         </div>

//         <div className="border border-t-0 border-blue-400 rounded-b bg-white px-4 py-3 text-blue-700">
//           <p className="text-center">{message}</p>
//           <div className="text-center">
//             <button
//               type="button"
//               className="mt-2 text-gray-800 bg-transparent border border-gray-700 hover:bg-blue-800 hover:text-white focus:ring-4 focus:outline-none focus:ring-gray-300 font-medium rounded-lg text-xs px-3 py-1.5 text-center dark:border-gray-600 dark:hover:bg-gray-600 dark:focus:ring-gray-800 dark:text-gray-300 dark:hover:text-white"
//               onClick={() => {
//                 setOpen(false);
//               }}
//             >
//               OK
//             </button>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// }

// export default CustomAlert;

// /******
//  *
//  * @Calling a Alert box
//  *
//  *
//  * message is used show message to user
//  * showAlert if show alert is true then alert is show otherwise not show.
//  */
// {
//   /* <CustomAlert message={"Data Not Found."} showAlert={true}/> */
// }
import React from "react";

export default function CustomAlert({ title, message, type, onClose }) {
  return (
    <div className="fixed inset-0 flex items-center justify-center z-50 ">
      <div className="bg-gray-100 rounded shadow-md w-1/3 ">
        <div className="bg-blue-900 text-white text-sm font-bold px-4 py-1.5 flex items-center justify-between ">
          <span className="flex w-full">
            <span className="flex px-1">
              {/* Conditionally render the success or error icon based on 'type' */}
              {/* {title === "Success" && (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                  className="w-5 h-5"
                >
                  <path
                    fillRule="evenodd"
                    d="M16.704 4.153a.75.75 0 01.143 1.052l-8 10.5a.75.75 0 01-1.127.075l-4.5-4.5a.75.75 0 011.06-1.06l3.894 3.893 7.48-9.817a.75.75 0 011.05-.143z"
                    clipRule="evenodd"
                  />
                </svg>
              )} */}
              {/* {title === "Error" && ( */}
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                className="w-6 h-6"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z"
                />
              </svg>
              {/* )} */}
              {/* Add more icons for different types if needed */}
            </span>
            <h2 className={`text-white `}>{title}</h2>
          </span>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="w-6 h-6 fill-current text-white"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M6 18L18 6M6 6l12 12"
            />
          </svg>
        </div>

        <p className={`text-${type}-500 items-center flex justify-center mt-2`}>
          {message}
        </p>
        <div className="mt-4 text-center">
          <button
            onClick={onClose}
            className={`bg-white text-black px-4 py-1 mb-1 mt-2 rounded-md border-2 hover:bg-blue-600 focus:outline-none`}
          >
            OK
          </button>
        </div>
      </div>
    </div>
  );
}
