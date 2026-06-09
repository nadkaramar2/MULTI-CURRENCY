import React, { useEffect, useState, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";

import { v4 as uuidv4 } from "uuid";
import amsApi from "../../api/amsApi";
import { Dialog, Transition } from "@headlessui/react";
import { NavLink } from "react-router-dom";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";

export default function AccountDetails() {
  const [alldata, setalldata] = useState([]);
  const [fromdate, setFromdate] = useState("");
  const [todate, setTodate] = useState("");
  const [searchKeyword, setSearchKeyword] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);
  let custid = localStorage.getItem("custid");
  let FirstName = localStorage.getItem("Firstname");
  let LastName = localStorage.getItem("LastName");

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  //   Pagination
  const [itemPage, setItemPage] = useState(10);
  const [tcount, setTcount] = useState(0);
  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  // Function to handle changes in the search input field
  const handleSearch = (event) => {
    setSearchKeyword(event.target.value);
  };

  // Filter the acceptData array based on the searchKeyword
  const filteredData = alldata.filter((data) => {
    const { strAccountType, strAccountNumber, strStatus, strClosingBalance } =
      data;
    const lowerCasedSearchKeyword = searchKeyword.toLowerCase();

    return (
      (strAccountType &&
        strAccountType.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strAccountNumber &&
        strAccountNumber.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strStatus &&
        strStatus.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strClosingBalance &&
        strClosingBalance.toLowerCase().includes(lowerCasedSearchKeyword))
    );
  });
  const highlightKeyword = (text, keyword) => {
    if (!keyword || typeof text !== "string") {
      return text; // No keyword to highlight or invalid text
    }

    const regex = new RegExp(`(${escapeRegExp(keyword)})`, "gi");
    const highlightedText = text.replace(
      regex,
      '<span className="bg-yellow-200">$1</span>'
    );
    return <div dangerouslySetInnerHTML={{ __html: highlightedText }} />;
  };
  const escapeRegExp = (string) => {
    return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"); // Escape special characters
  };
  useEffect(() => {
    categoryListModelsList();
  }, []);

  const categoryListModelsList = async () => {
    if (!custid) {
      // showError("Please check Customer ");
    } else {
      try {
        const response = await amsApi.post(
          `account/getAccountInfoListBasdOnTypes`,
          {
            strCustId: custid,
          }
        );

        if (response.data.code === "S0000") {
          setalldata(response.data.accountInfoList);
          setCheckpoint(1);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  let [isOpen, setIsOpen] = useState(false);
  function closeModal() {
    setIsOpen(false);
  }

  function openModal() {
    setIsOpen(true);
  }

  const [selectedRowData, setSelectedRowData] = useState(null);

  function openModal(rowData) {
    const combinedInfo = `${rowData.strAccountType}-${rowData.strAccountNumber}`;
    setSelectedRowData(combinedInfo);
    // setSelectedRowData(rowData);
    setIsOpen(true);
  }

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full ">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-8 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between ">
              <p className="text-base  sm:text-xm  text-black :text-2xl  leading-normal ">
                Customer Basic Information
              </p>
            </div>
          </div>
        </div>

        <div className=" md:col-span-2 lg:col-span-1  bg-white">
          <form className="">
            <div className="overflow-hidden shadow sm:rounded-md">
              <div className=" px-4 py-1 sm:p-2 bg-white  ">
                <div className="grid gap-4 mb-1 md:grid-cols-3 px-8">
                  <div>
                    <label
                      htmlFor="GL Account Type"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Customer Id
                    </label>
                    <input
                      type="text"
                      id="strCustId"
                      defaultValue={custid}
                      disabled
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Customer id"
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="GL Account Description"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Customer Name
                    </label>
                    <input
                      type="text"
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Customer name"
                      defaultValue={
                        FirstName && LastName ? FirstName + " " + LastName : ""
                      }
                    />
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>

        {/* table */}
        <div className="bg-white overflow-x-auto relative shadow-md  mt-1 px-2">
          <div className="overflow-x-auto relative shadow-md  ">
            <h2 className="font-normal md:font-bold">Account Details</h2>
          </div>
        </div>

        <div className="flex justify-end items-center sm:px-6 lg:px-8 bg-blue-100 rounded-t-lg ">
          <div className=" flex justify-end  rounded-md border border-transparent  px-2 mx-2 text-sm  font-medium text-white ">
            <div className="pt-2 relative  mx-auto text-gray-600">
              <input
                title="Search Data"
                type="text"
                value={searchKeyword}
                onChange={handleSearch}
                className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                placeholder="Search.."
                style={{
                  backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                  // You can set other styles here as needed
                }}
                disabled={checkpoint !== 1}
              />
              <button
                type="submit"
                className="absolute right-0 top-0 mt-4 mr-2   "
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>
        <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={filteredData[0]?.strTotalCount}
          reCallApi={categoryListModelsList}
        />

        <div className="table-wrp block max-h-[22rem] ">
          <table className="w-full text-xs text-left text-clack dark:text-blue-100">
            <thead className="text-xm border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
              <tr>
                <th scope="col" className="py-1.5 px-6">
                  A/C TYPE
                </th>
                <th scope="col" className="py-1.5 px-6">
                  A/C NUMBER
                </th>
                <th scope="col" className="py-1.5 px-6">
                  STATUS
                </th>
                <th scope="col" className="py-1.5 px-6">
                  STATEMENT
                </th>
                <th scope="col" className="py-1.5 px-6">
                  BALANCE
                </th>
              </tr>
            </thead>
            <tbody>
              {filteredData.length > 0 ? (
                <>
                  {filteredData.map(
                    ({
                      strAccountType,
                      strAccountNumber,
                      strStatus,
                      strClosingBalance,
                    }) => (
                      <tr
                        key={uuidv4()}
                        className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                      >
                        <td className="px-6 py-1 whitespace-nowrap">
                          <span className="text-xs font-medium text-gray-900">
                            {highlightKeyword(
                              strAccountType || "-",
                              searchKeyword
                            )}
                          </span>
                        </td>
                        <td className="px-6 py-1.5 whitespace-nowrap">
                          <span className="text-xs font-medium text-gray-900">
                            {highlightKeyword(
                              strAccountNumber || "-",
                              searchKeyword
                            )}
                          </span>
                        </td>
                        <td className="px-6 py-1.5 whitespace-nowrap">
                          <span className="text-xs font-medium text-gray-900">
                            {highlightKeyword(strStatus || "-", searchKeyword)}
                          </span>
                        </td>
                        <td
                          className="px-6 py-1.5 whitespace-nowrap"
                          onClick={() =>
                            openModal({
                              strAccountType,
                              strAccountNumber,
                              strStatus,
                              strClosingBalance,
                            })
                          }
                        >
                          <button
                            title="Click View Button"
                            className="inline-flex items-center py-1 px-2 text-sm font-medium sm:rounded-md text-white bg-blue-600   hover:bg-gray-100 hover:text-blue-800 focus:z-10 focus:ring-2 focus:ring-blue-800 focus:text-blue-800 dark:bg-gray-700 dark:border-gray-600 dark:text-white dark:hover:text-white dark:hover:bg-gray-600 dark:focus:ring-blue-500 dark:focus:text-white"
                          >
                            View
                            <svg
                              className="w-5 h-4 ml-1"
                              fill="currentColor"
                              viewBox="0 0 20 20"
                              xmlns="http://www.w3.org/2000/svg"
                            >
                              <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"></path>
                              <path
                                fillRule="evenodd"
                                d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z"
                                clipRule="evenodd"
                              ></path>
                            </svg>
                          </button>
                        </td>
                        <td className="px-6 py-1.5 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {strClosingBalance || "-"}
                          </span>
                        </td>
                      </tr>
                    )
                  )}
                </>
              ) : (
                <tr>
                  <td colSpan="6" className="px-6 py-1 text-center">
                    <span className="text-sm font-normal text-gray-500">
                      No data available.
                    </span>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
      {/* </div> */}
      {/* </div> */}

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}

      {/* Dilog box */}
      <Transition appear show={isOpen} as={Fragment}>
        <Dialog as="div" className="relative z-10" onClose={closeModal}>
          <Transition.Child
            as={Fragment}
            enter="ease-out duration-300"
            enterFrom="opacity-0"
            enterTo="opacity-100"
            leave="ease-in duration-200"
            leaveFrom="opacity-100"
            leaveTo="opacity-0"
          >
            <div className="fixed inset-0 bg-black bg-opacity-25" />
          </Transition.Child>

          <div className="fixed inset-0 overflow-y-auto">
            <div className="flex min-h-full items-center justify-center p-4 text-center">
              <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0 scale-95"
                enterTo="opacity-100 scale-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100 scale-100"
                leaveTo="opacity-0 scale-95"
              >
                <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                  <Dialog.Title
                    as="h3"
                    className="text-sm font-normal leading-6 text-blue-900"
                  >
                    Search Account Statement
                  </Dialog.Title>
                  <div className="mt-2">
                    <div className="overflow-hidden shadow sm:rounded-md">
                      <div className="bg-slate-100 px-4 py-3 sm:p-6">
                        <div className="col-span-6 sm:col-span-3 font-bold text-blue-700">
                          <form className=" space-y-4">
                            <div className="rounded-md space-y-2">
                              <div>
                                <label htmlFor="fromdate">Start Date</label>
                                <input
                                  id="fromdate"
                                  name="fromdate"
                                  type="date"
                                  value={fromdate}
                                  onChange={(e) => setFromdate(e.target.value)}
                                  autoComplete="off"
                                  className="appearance-none  relative block w-full px-3 py-1 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                                  placeholder="username"
                                />
                              </div>
                              <div>
                                <label htmlFor="todate">End Date</label>
                                <input
                                  id="todate"
                                  name="todate"
                                  type="date"
                                  value={todate}
                                  onChange={(e) => setTodate(e.target.value)}
                                  autoComplete="off"
                                  className="appearance-none  relative block w-full px-3 py-1 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                                  placeholder="Password"
                                />
                              </div>
                            </div>
                          </form>
                        </div>
                        {""}
                      </div>
                    </div>
                  </div>

                  <div className="mt-2flex space-x-2 justify-end items-end sm:px-2">
                    {/* Rest of the dialog content */}
                  </div>

                  <div className="mt-2 flex space-x-2 justify-end items-end sm:px-2">
                    <NavLink
                      title="Click Here to link"
                      to={`/account-statement/${fromdate}/${todate}/${selectedRowData}`}
                      className="text-white bg-blue-500 focus:outline-none focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-1 mr-2 dark:bg-gray-800 dark:hover:bg-gray-700 dark:focus:ring-gray-700 dark:border-gray-700"
                    >
                      Search
                    </NavLink>

                    <button
                      title="Cancel Here"
                      type="button"
                      className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1 px-5 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                      onClick={closeModal}
                    >
                      Cancel
                    </button>
                  </div>
                </Dialog.Panel>
              </Transition.Child>
            </div>
          </div>
        </Dialog>
      </Transition>
    </AppLayout>
  );
}
