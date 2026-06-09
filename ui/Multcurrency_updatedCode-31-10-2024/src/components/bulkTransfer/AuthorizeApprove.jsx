import React, { useState, useEffect, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";

import { useParams } from "react-router-dom";
import { v4 as uuidv4 } from "uuid";
import { Dialog, Transition } from "@headlessui/react";
import { useNavigate } from "react-router-dom";
import { ArrowLeftIcon } from "@heroicons/react/24/solid";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";
import AutoPagintation from "../../UI/AutoPagintation";
export default function AuthorizeApprove() {
  const navigate = useNavigate();
  const [data, setData] = useState([]);
  const { strBulkMode } = useParams();
  const { strPreTransactionId } = useParams();
  const [resion, setResion] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);

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

  useEffect(() => {
    BulkTransferTransactions();
  }, []);

  const BulkTransferTransactions = async () => {
    if (!strBulkMode || !strPreTransactionId) {
      swal("Please check your Bulk Mode and participantID ");
    } else {
      try {
        const response = await amsApi.post(
          `bulkTransfer/authorizePreTxnBulklist`,
          {
            strPreTransactionId: strPreTransactionId,
            strBulkMode: strBulkMode,
          },

          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.status === 200) {
          if (response.data === "Bulk Transfer Not Found") {
            setData([]);
          } else {
            setData(response.data);
            setCheckpoint(1);
          }
        } else {
          handleShowError("Bulk Transfer list Not Found");
        }
      } catch (error) {
        handleShowError("Bulk Transfer list Not Found");
      }
    }
  };

  const Approve = async () => {
    if (!strBulkMode || !strPreTransactionId === "") {
      swal("Please check your Bulk Mode and participantID ");
    } else {
      try {
        const response = await amsApi.post(
          `bulkTransfer/approve`,
          {
            strPreTransactionId: strPreTransactionId,
            strBulkMode: strBulkMode,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          handleShowSuccess("Sucessfully Approved the Bulk Entries");
          //  navigate("/authorize", { replace: true });
        } else {
          handleShowError("Bulk Mode not found!!");
        }
      } catch (error) {
        handleShowError("Bulk Mode not found!!");
      }
    }
  };

  // Reject

  const reject = async () => {
    if (!strBulkMode || !strPreTransactionId === "") {
      swal("Please check your Bulk Mode and participantID ");
    } else if (!resion) {
      swal("Please Enter  Reasion For Reject ");
    } else {
      try {
        const response = await amsApi.post(
          `bulkTransfer/reject`,
          {
            strPreTransactionId: strPreTransactionId,
            strBulkMode: strBulkMode,
            strRejectedReason: resion,
          },

          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          // showSuccess(response.data);
          handleShowSuccess("Sucessfully Reject the Bulk Entries");
          //  navigate("/authorize", { replace: true });
        } else {
          handleShowError(response.data);
        }
      } catch (error) {
        handleShowError(error.response.data);
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

  // sorted tabled data
  const [sortColumn, setSortColumn] = useState(""); // State to track the sorted column
  const [sortOrder, setSortOrder] = useState("asc"); // State to track sorting order
  const handleSort = (column) => {
    if (column === sortColumn) {
      // If clicking on the same column, toggle the sorting order
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      // If clicking on a different column, set the new column and default to ascending order
      setSortColumn(column);
      setSortOrder("asc");
    }
  };
  const renderSortArrow = (column) => {
    if (column === sortColumn) {
      return sortOrder === "asc" ? "↑" : "↓";
    }
    return "↕";
  };
  // Sort the data based on the chosen column and order
  const sortedData = [...data].sort((a, b) => {
    if (sortOrder === "asc") {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return aValue.localeCompare(bValue);
    } else {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return bValue.localeCompare(aValue);
    }
  });

  return (
    <AppLayout>
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4  bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Selected Authorization List
              </p>

              {/* <div className="flex justify-center rounded-md border border-transparent  mx-2 text-sm font-medium text-white "> */}
              <div className="pt-2 relative mx-auto text-gray-600">
                <input
                  title="Search Data"
                  type="search"
                  id="search"
                  className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  placeholder="Search.."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  style={{
                    backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                  }}
                  disabled={checkpoint !== 1}
                />
                <button type="submit" className="absolute right-0 top-0 mt-3 ">
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
                {/* </div> */}
              </div>
              <button
                title="Go back"
                type="button"
                onClick={() => navigate(-1)}
                className="inline-flex justify-center rounded-md border border-transparent bg-gray-500 py-0.5 px-2  text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
              >
                <ArrowLeftIcon className="h-4 w-4" />
              </button>
            </div>
          </div>
        </div>
        {/* <h1 className="font-medium flex bg-blue-300 justify-center  text-blue-800 text-2xl ">
          Selected Authorization List
        </h1> */}

        <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={data[0]?.strTotalCount}
          reCallApi={BulkTransferTransactions}
        />
        <div className="overflow-x-auto relative shadow-md ">
          <div className="table-wrp block max-h-[27rem]  max-w-[27rem]   ">
            <table className="w-full text-sm text-left text-black dark:text-blue-100">
              <thead className="text-xs border-b sticky font-bold top-0 text-gray-500 uppercase bg-gray-100 dark:text-white">
                <tr>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strFromAccountType")}
                  >
                    From A/c Type
                    {renderSortArrow("strFromAccountType")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strFromAccountNo")}
                  >
                    From A/c No
                    {renderSortArrow("strFromAccountNo")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strFromAccountName")}
                  >
                    From A/c Name
                    {renderSortArrow("strFromAccountName")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strToAccountType")}
                  >
                    To A/c Type
                    {renderSortArrow("strToAccountType")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strToAccountNo")}
                  >
                    To A/c No
                    {renderSortArrow("strToAccountNo")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strToAccountName")}
                  >
                    To A/c Name
                    {renderSortArrow("strToAccountName")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strAmount")}
                  >
                    Amount To Transfer
                    {renderSortArrow("strAmount")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strMakerId")}
                  >
                    Maker User Id
                    {renderSortArrow("strMakerId")}
                  </th>
                  <th
                    scope="col"
                    className="py-2.5 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strNarration")}
                  >
                    Narration
                    {renderSortArrow("strNarration")}
                  </th>
                </tr>
              </thead>

              <tbody>
                {sortedData
                  .filter(
                    (data) =>
                      data.strFromAccountType
                        .toLowerCase()
                        .includes(searchQuery.toLowerCase()) ||
                      data.strFromAccountNo
                        .toLowerCase()
                        .includes(searchQuery.toLowerCase()) ||
                      data.strFromAccountName
                        .toLowerCase()
                        .includes(searchQuery.toLowerCase()) ||
                      data.strToAccountType
                        .toLowerCase()
                        .includes(searchQuery.toLowerCase()) ||
                      data.strToAccountNo
                        .toLowerCase()
                        .includes(searchQuery.toLowerCase()) ||
                      data.strToAccountName
                        .toLowerCase()
                        .includes(searchQuery.toLowerCase()) ||
                      data.strMakerId
                        .toLowerCase()
                        .includes(searchQuery.toLowerCase())
                  )
                  .map((data) => (
                    <tr
                      key={uuidv4()}
                      className=" border-b dark:border-neutral-500 "
                    >
                      <td className="px-6 py-2.5 whitespace-nowrap flex text-center">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strFromAccountType || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strFromAccountNo || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strFromAccountName || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strToAccountType || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strToAccountNo || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strToAccountName || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 text-end whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strAmount || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strMakerId || "-"}
                        </div>
                      </td>
                      <td className="px-6 py-2.5 whitespace-nowrap ">
                        <div className="text-sm font-medium text-gray-900">
                          {data.strNarration || "-"}
                        </div>
                      </td>
                    </tr>
                  ))}
              </tbody>
            </table>
          </div>
        </div>
        {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
        <div className="bg-gray-100 px-6 py-1 space-x-2 text-right sm:px-6  ">
          <button
            title="Click Approve Data"
            type="button"
            onClick={Approve}
            data-modal-toggle="defaultModal"
            className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
          >
            Approve
          </button>
          <button
            title="Click Approve Data"
            type="button"
            onClick={openModal}
            data-modal-toggle="defaultModal"
            className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
          >
            Reject
          </button>
        </div>
      </div>

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
                    className="text-lg font-medium leading-6 text-blue-900"
                  >
                    Reason for Reject
                  </Dialog.Title>
                  <div className="mt-2">
                    <div className="overflow-hidden shadow sm:rounded-md">
                      <div className="bg-slate-100 px-6 py-5 sm:p-6">
                        <div className="col-span-6 sm:col-span-3 font-bold text-blue-700">
                          <textarea
                            type="text"
                            id="amount"
                            value={resion}
                            onChange={(e) => setResion(e.target.value)}
                            className="border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                            placeholder="Enter Account number"
                          />
                        </div>
                        {""}
                      </div>
                    </div>
                  </div>

                  <div className="mt-4 flex space-x-2 justify-end items-end sm:px-6">
                    <button
                      title="Click Reject Data"
                      type="button"
                      onClick={reject}
                      className="inline-flex justify-center rounded-md  border border-transparent bg-blue-100 px-6 py-2 text-sm font-medium text-blue-900 hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-offset-2"
                    >
                      Reject
                    </button>
                    <button
                      title="Click Cancel Data"
                      type="button"
                      className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2.5 px-5 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
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
