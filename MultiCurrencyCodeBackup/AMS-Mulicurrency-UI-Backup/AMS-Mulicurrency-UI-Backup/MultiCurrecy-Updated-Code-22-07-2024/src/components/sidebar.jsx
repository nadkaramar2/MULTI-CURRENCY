import React, { useEffect, useState } from "react";
import { Link, Route, Routes } from "react-router-dom";
import { Disclosure } from "@headlessui/react";
import amsApi from "../api/amsApi";
import download from "../assets/img/download.png";
import control from "../assets/img/control.png";

import {
  Bars3Icon,
  ChevronLeftIcon,
  CodeBracketIcon,
  XMarkIcon,
} from "@heroicons/react/24/outline";

import {
  ChevronDownIcon,
  ChevronUpIcon,
  PlusIcon,
  MinusIcon,
  HomeIcon,
} from "@heroicons/react/24/solid";

import { v4 as uuvid } from "uuid";

import { useSelector } from "react-redux";
import AppLayout from "../layout/AppLayout";
const Sidebar = () => {
  const [open, setOpen] = useState(true);
  localStorage.setItem("OpenModel", open);

  const { authInfo } = useSelector((state) => state.auth);

  const userRole = authInfo.roleId;

  const [collapseShow, setCollapseShow] = useState("hidden");

  const [allData, setAllData] = useState([]);

  const [time, setTime] = useState({
    minutes: new Date().getMinutes(),

    hours: new Date().getHours(),

    seconds: new Date().getSeconds(),
  });

  useEffect(() => {
    const intervalId = setInterval(() => {
      const date = new Date();

      setTime({
        minutes: date.getMinutes(),

        hours: date.getHours(),

        seconds: date.getSeconds(),
      });
    }, 1000);

    return () => clearInterval(intervalId);
  }, []);

  const convertToTwoDigit = (number) => {
    return number.toLocaleString("en-US", {
      minimumIntegerDigits: 2,
    });
  };

  useEffect(() => {
    menuApi();
  }, []);

  const menuApi = async () => {
    try {
      const response = await amsApi.post(
        `/useraccessmenu/menusByRole`,

        {
          strMenuRoleId: userRole,
        },

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setAllData(response.data.menuResponseList);
      } else {
        alert(response.data.status);
      }
    } catch (error) {}
  };

  const [activeMenuItem, setActiveMenuItem] = useState(null);

  const handleMenuItemClick = (menuItem) => {
    console.log("menutem :  " + menuItem);
    //setActiveMenuItem(menuItem);
    // setActiveMenuItem(menuItem === activeMenuItem ? null : menuItem);
    //    setActiveMenuItem((prevMenuItem) =>
    //    prevMenuItem === menuItem ? null : menuItem
    //  );
    // setActiveMenuItem((prevMenuItem) => {
    //     if (activeMenuItem === menuItem) {
    //       return null;
    //     } else {
    //       return menuItem;
    //     }
    //   });
    setActiveMenuItem((prevMenuItem) => {
      if (prevMenuItem === menuItem) {
        console.log(" prevMenuItem menutem:   " + menuItem);
        return prevMenuItem;
      } else {
        console.log(" New menutem:   " + menuItem);
        return menuItem;
      }
    });
  };

  return (
    <div className="flex">
      <div
        className={` ${
          open ? "w-64" : "w-20"
        } bg-white h-screen p-5 relative duration-300 overflow-auto`}
      >
        <div className="flex justify-between sticky top-2 bg-blue-600 rounded-md">
          {" "}
          <img
            src={control}
            alt="img loading.."
            className={`absolute cursor-pointer -right-3 top-4 w-8 border-dark-purple

           border-2 rounded-full  stroke-black ${!open && "rotate-180"}`}
            onClick={() => {
              setOpen(!open);
            }}
          />
          <div className="flex gap-x-4 items-center">
            <img
              alt="img loading.."
              src={download}
              className={`cursor-pointer duration-500  ${
                open && "rotate-[360deg]"
              }`}
            />

            <Link
              exact="true"
              to="/dashboard"
              className={`text-white origin-left font-medium text-xl duration-200 capitalize mx-6 ${
                !open && "scale-0"
              }`}
            >
              <h1>AMS</h1>
            </Link>
          </div>
        </div>
        <div
          className={` flex flex-col justify-start w-full space-y-1 py-1 pt-4 mb-10  ${
            !open && "scale-0"
          }`}
        >
          {/* <button className="bg-blue-200 hover:bg-blue-300 text-md py-2 border border-blue-700 rounded">
              <div className="clock">
                <span>
                  {userRole} {" : "}
                </span>

                <span>{convertToTwoDigit(time.hours)}:</span>

                <span>{convertToTwoDigit(time.minutes)}:</span>

                <span>{convertToTwoDigit(time.seconds)}</span>
              </div>
            </button> */}

          {allData.map((data, index) => (
            <Disclosure key={index}>
              {({ open }) => (
                <>
                  <div className="pt-1">
                    <Disclosure.Button
                      onClick={() => handleMenuItemClick(data.parentMenuName)}
                      className="w-full flex items-center justify-between   py-1.5 px-2 text-base text-blue-600 shadow-md rounded-lg bg-blue-100"
                    >
                      <span
                        className={`${open} origin-left duration-200 font-roboto text-xs text-start `}
                      >
                        {data.parentMenuName}
                      </span>

                      <span className="flex items-center">
                        {activeMenuItem === data.parentMenuName && open ? (
                          <MinusIcon
                            className="h-5 w-5 text-blue-600"
                            aria-hidden="true"
                          />
                        ) : (
                          <PlusIcon
                            className="h-5 w-5 text-blue-800"
                            aria-hidden="true"
                          />
                        )}
                      </span>
                    </Disclosure.Button>
                  </div>
                  {activeMenuItem === data.parentMenuName && (
                    <Disclosure.Panel>
                      {data.userMenuList.map((d) => (
                        <Link
                          key={uuvid()}
                          to={d.strMenuDescription}
                          onClick={() => {
                            setCollapseShow("hidden");
                            setOpen(!open);
                          }}
                          className="focus:outline-none flex text-gray-700
  
                                     text-xs  jusitfy-start hover:text-white focus:bg-emerald-500 focus:text-white hover:bg-blue-200  rounded py-1 px-1 items-center w-full  p-2 bg-gray-100 my-1 "
                        >
                          <p className="text-xs leading-4 pl-1 text-gray-700 font-normal">
                            {d.strMenuName}
                          </p>
                        </Link>
                      ))}
                    </Disclosure.Panel>
                  )}
                </>
              )}
            </Disclosure>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Sidebar;

// import React, { useEffect, useState } from "react";
// import { Link } from "react-router-dom";
// import { Disclosure } from "@headlessui/react";
// import amsApi from "../api/amsApi";
// import download from "../assets/img/download.png";
// import control from "../assets/img/control.png";

// // import { HiPlusSm } from "react-icons/hi";

// import { v4 as uuvid } from "uuid";
// import { useSelector } from "react-redux";
// import AppLayout from "../layout/AppLayout";
// const Sidebar = () => {
//   const [open, setOpen] = useState(true);
//   localStorage.setItem("OpenModel", open);

//   const { authInfo } = useSelector((state) => state.auth);

//   const userRole = authInfo.roleId;

//   const [collapseShow, setCollapseShow] = useState("hidden");

//   const [allData, setAllData] = useState([]);

//   const [time, setTime] = useState({
//     minutes: new Date().getMinutes(),

//     hours: new Date().getHours(),

//     seconds: new Date().getSeconds(),
//   });

//   useEffect(() => {
//     const intervalId = setInterval(() => {
//       const date = new Date();

//       setTime({
//         minutes: date.getMinutes(),

//         hours: date.getHours(),

//         seconds: date.getSeconds(),
//       });
//     }, 1000);

//     return () => clearInterval(intervalId);
//   }, []);

//   const convertToTwoDigit = (number) => {
//     return number.toLocaleString("en-US", {
//       minimumIntegerDigits: 2,
//     });
//   };

//   useEffect(() => {
//     menuApi();
//   }, []);

//   const menuApi = async () => {
//     try {
//       const response = await amsApi.post(
//         `/useraccessmenu/menusByRole`,

//         {
//           strMenuRoleId: userRole,
//         },

//         {
//           headers: {
//             "Content-Type": "application/json",
//           },
//         }
//       );

//       if (response.data.code === "S0000") {
//         setAllData(response.data.menuResponseList);
//       } else {
//         alert(response.data.status);
//       }
//     } catch (error) {}
//   };

//   return (
//     <div className="flex ">
//       <div
//         className={` ${
//           open ? "w-72" : "w-20"
//         } bg-white h-screen p-5 relative duration-300 overflow-auto`}
//       >
//         <img
//           src={control}
//           className={`absolute cursor-pointer -right-3 top-6 w-8 border-dark-purple

//            border-2 rounded-full mr-2 ${!open && "rotate-180"}`}
//           onClick={() => setOpen(!open)}
//         />
//         <div className="flex gap-x-4 items-center">
//           <img
//             src={download}
//             className={`cursor-pointer duration-500 ${
//               open && "rotate-[360deg]"
//             }`}
//           />

//           <Link
//             exact="true"
//             to="/dashboard"
//             className={`text-blue-600 origin-left font-medium text-xl duration-200 capitalize ${
//               !open && "scale-0"
//             }`}
//           >
//             <h1>AMS</h1>
//           </Link>
//         </div>
//         <div
//           className={` flex flex-col justify-start w-full space-y-1 py-1 pt-4 ${
//             !open && "scale-0"
//           }`}
//         >
//           {/* <button className="bg-blue-200 hover:bg-blue-300 text-md py-2 border border-blue-700 rounded">
//               <div className="clock">
//                 <span>
//                   {userRole} {" : "}
//                 </span>

//                 <span>{convertToTwoDigit(time.hours)}:</span>

//                 <span>{convertToTwoDigit(time.minutes)}:</span>

//                 <span>{convertToTwoDigit(time.seconds)}</span>
//               </div>
//             </button> */}

//           {allData.map((data) => (
//             <Disclosure as="div">
//               {({ open }) => (
//                 <>
//                   <div className="pt-1">
//                     <Disclosure.Button className="w-full flex items-center justify-between py-2 px-2 text-base text-blue-600 shadow-md rounded-lg bg-blue-100">
//                       <span
//                         className={`${
//                           open && open
//                         } origin-left duration-200 font-semibold`}
//                       >
//                         {data.parentMenuName}
//                       </span>

//                       <span className="flex items-center">
//                         {open ? (
//                           <svg
//                             xmlns="http://www.w3.org/2000/svg"
//                             viewBox="0 0 24 24"
//                             fill="currentColor"
//                             className="w-6 h-6"
//                           >
//                             <path
//                               fillRule="evenodd"
//                               d="M5.25 12a.75.75 0 01.75-.75h12a.75.75 0 010 1.5H6a.75.75 0 01-.75-.75z"
//                               clipRule="evenodd"
//                             />
//                           </svg>
//                         ) : (
//                           <svg
//                             xmlns="http://www.w3.org/2000/svg"
//                             viewBox="0 0 24 24"
//                             fill="currentColor"
//                             className="w-6 h-6"
//                           >
//                             <path
//                               fillRule="evenodd"
//                               d="M12 5.25a.75.75 0 01.75.75v5.25H18a.75.75 0 010 1.5h-5.25V18a.75.75 0 01-1.5 0v-5.25H6a.75.75 0 010-1.5h5.25V6a.75.75 0 01.75-.75z"
//                               clipRule="evenodd"
//                             />
//                           </svg>
//                         )}
//                       </span>
//                     </Disclosure.Button>
//                   </div>

//                   <Disclosure.Panel>
//                     {data.userMenuList.map((d) => (
//                       <Link
//                         key={uuvid()}
//                         to={d.strMenuDescription}
//                         onClick={() => {
//                           setCollapseShow("hidden");
//                         }}
//                         className="focus:outline-none flex text-gray-700

//                   font-normal  jusitfy-start hover:text-white focus:bg-emerald-500 focus:text-white hover:bg-green-600  rounded py-2 px-1 items-center w-full shadow-lg p-2 bg-blue-200 my-2"
//                       >
//                         <p className="text-sm leading-4 pl-3">
//                           {d.strMenuName}
//                         </p>
//                       </Link>
//                     ))}
//                   </Disclosure.Panel>
//                 </>
//               )}
//             </Disclosure>
//           ))}
//         </div>
//       </div>
//     </div>
//   );
// };

// export default Sidebar;
